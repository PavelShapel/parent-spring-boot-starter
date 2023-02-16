package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.PutRequest;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.pavelshapel.aop.spring.boot.starter.annotation.ExceptionWrapped;
import com.pavelshapel.aws.spring.boot.starter.MaxRetryCountException;
import com.pavelshapel.aws.spring.boot.starter.api.service.DynamoDbService;
import com.pavelshapel.aws.spring.boot.starter.api.service.ToScanExpressionConverter;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.util.ClassUtils;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.pavelshapel.core.spring.boot.starter.api.model.Entity.ID_FIELD;
import static java.util.Objects.nonNull;
import static java.util.function.Predicate.not;
import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.getField;
import static org.springframework.util.ReflectionUtils.makeAccessible;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExceptionWrapped
public abstract class AbstractDynamoDbService<T extends Entity<String>> implements DynamoDbService<T> {
    public static final int TEN = 10;
    @Autowired
    DynamoDBMapper dynamoDBMapper;
    @Autowired
    ClassUtils classUtils;
    @Autowired
    ToScanExpressionConverter toScanExpressionConverter;

    @Override
    public T save(T entity) {
        dynamoDBMapper.save(entity);
        return entity;
    }


    @Override
    public List<T> saveAll(List<T> entities) {
        return saveAll(entities, 1);
    }

    @SneakyThrows
    private List<T> saveAll(List<T> entities, long retryCount) {
        if (retryCount > TEN) {
            throw new MaxRetryCountException(TEN);
        }
        TimeUnit.MILLISECONDS.sleep(retryCount * retryCount * TEN);
        Optional.ofNullable(entities)
                .filter(not(CollectionUtils::isEmpty))
                .map(dynamoDBMapper::batchSave)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(this::getFailedBatchSavedItems)
                .forEach(items -> saveAll(items, retryCount + 1));
        return entities;
    }

    private List<T> getFailedBatchSavedItems(DynamoDBMapper.FailedBatch failedBatch) {
        return failedBatch.getUnprocessedItems()
                .values()
                .stream()
                .flatMap(List::stream)
                .map(WriteRequest::getPutRequest)
                .map(PutRequest::getItem)
                .map(item -> dynamoDBMapper.marshallIntoObject(getEntityClass(), item))
                .collect(Collectors.toList());
    }

    @Override
    public T findById(String id) {
        return dynamoDBMapper.load(getEntityClass(), id);
    }

    @Override
    public List<T> findAll(Set<SearchCriterion> searchCriteria, Pageable pageable) {
        DynamoDBScanExpression dynamoDBScanExpression = toScanExpressionConverter.convert(searchCriteria);
        Pageable verifiedPageable = createDefaultPageableIfNull(pageable);
        return dynamoDBMapper.scan(getEntityClass(), dynamoDBScanExpression).stream()
                .sorted(createPageableComparator(verifiedPageable))
                .skip((long) verifiedPageable.getPageNumber() * verifiedPageable.getPageSize())
                .limit(verifiedPageable.getPageSize())
                .collect(Collectors.toList());
    }

    private Pageable createDefaultPageableIfNull(Pageable pageable) {
        return Optional.ofNullable(pageable)
                .orElseGet(() -> {
                    int count = dynamoDBMapper.count(getEntityClass(), new DynamoDBScanExpression());
                    return PageRequest.of(0, count > 0 ? count : 1, Sort.by(ID_FIELD).ascending());
                });
    }

    private Comparator<T> createPageableComparator(Pageable pageable) {
        String fieldName = pageable.getSort().stream()
                .findFirst()
                .map(Sort.Order::getProperty)
                .orElseThrow();
        boolean isDescending = pageable.getSort().stream()
                .map(Sort.Order::getDirection)
                .anyMatch(Sort.Direction::isDescending);
        Comparator<T> comparator = Comparator.comparing(entity -> getFieldValue(entity, fieldName));
        if (isDescending) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    private Comparable getFieldValue(T entity, String fieldName) {
        return Optional.ofNullable(fieldName)
                .filter(unused -> nonNull(entity))
                .map(name -> findField(getEntityClass(), name))
                .map(this::makeFieldAccessible)
                .map(field -> getField(field, entity))
                .map(Comparable.class::cast)
                .orElseThrow();
    }

    private Field makeFieldAccessible(Field field) {
        makeAccessible(field);
        return field;
    }

    @Override
    public void deleteById(String id) {
        dynamoDBMapper.delete(findById(id));
    }

    @Override
    public Class<T> getEntityClass() {
        return classUtils.getGenericSuperclass(getClass())
                .map(entityClass -> (Class<T>) entityClass)
                .orElseThrow();
    }
}
