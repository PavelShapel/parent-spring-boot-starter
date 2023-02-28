package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTransactionWriteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.pavelshapel.aop.spring.boot.starter.annotation.ExceptionWrapped;
import com.pavelshapel.aws.spring.boot.starter.api.service.DynamoDbService;
import com.pavelshapel.aws.spring.boot.starter.api.service.ScanExpressionFromSearchCriteriaConverter;
import com.pavelshapel.aws.spring.boot.starter.api.service.TransactionWriteExpressionFromSearchCriteriaConverter;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity;
import com.pavelshapel.core.spring.boot.starter.api.util.ClassUtils;
import com.pavelshapel.core.spring.boot.starter.enums.PrimitiveType;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pavelshapel.core.spring.boot.starter.api.model.Entity.ID_FIELD;
import static com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity.PARENT_FIELD;
import static com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity.ROOT_ID;
import static com.pavelshapel.jpa.spring.boot.starter.service.search.SearchOperation.EQUALS;
import static com.pavelshapel.jpa.spring.boot.starter.service.search.SearchOperation.IS_NOT_NULL;
import static com.pavelshapel.jpa.spring.boot.starter.service.search.SearchOperation.IS_NULL;
import static java.util.Objects.isNull;
import static java.util.function.Predicate.not;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExceptionWrapped
public abstract class AbstractDynamoDbService<T extends Entity<String>> implements DynamoDbService<T> {
    @Autowired
    DynamoDBMapper dynamoDBMapper;
    @Autowired
    ClassUtils classUtils;
    @Autowired
    ScanExpressionFromSearchCriteriaConverter scanExpressionFromSearchCriteriaConverter;
    @Autowired
    TransactionWriteExpressionFromSearchCriteriaConverter transactionWriteExpressionFromSearchCriteriaConverter;

    @Override
    public T save(T entity) {
        return Optional.of(entity)
                .map(List::of)
                .map(this::saveAll)
                .map(CollectionUtils::firstElement)
                .orElseThrow();
    }

    @Override
    public List<T> saveAll(List<T> entities) {
        TransactionWriteRequest transactionWriteRequest = new TransactionWriteRequest();
        Map<ParentalEntity<?, ?>, DynamoDBTransactionWriteExpression> transactionConditionChecks = new HashMap<>();
        Optional.of(entities)
                .filter(not(CollectionUtils::isEmpty))
                .orElseThrow()
                .stream()
                .flatMap(this::addRootConstraintEntity)
                .map(entity -> addTransactionWriteExpression(entity, transactionWriteRequest))
                .forEach(entity -> createConditionCheckForRootConstraintEntity(entity, transactionConditionChecks));
        transactionConditionChecks.forEach(transactionWriteRequest::addConditionCheck);
        executeTransactionWriteRequest(transactionWriteRequest);
        return entities;
    }

    private Stream<T> addRootConstraintEntity(T entity) {
        return Optional.of(entity)
                .filter(ParentalEntity.class::isInstance)
                .map(ParentalEntity.class::cast)
                .filter(parentalEntity -> isNull(parentalEntity.getParent()))
                .map(parentalEntity -> Stream.of((T) parentalEntity, createRootConstraintEntity()))
                .orElseGet(() -> Stream.of(entity));
    }

    private T createRootConstraintEntity() {
        T rootConstraintEntity = create();
        rootConstraintEntity.setId(ROOT_ID);
        return rootConstraintEntity;
    }

    private T addTransactionWriteExpression(T entity, TransactionWriteRequest transactionWriteRequest) {
        String id = entity.getId();
        if (isNull(id) || ROOT_ID.equals(id)) {
            return addPutWithIdNullTransactionWriteExpression(entity, transactionWriteRequest);
        } else {
            return addUpdateWithIdNotNullTransactionWriteExpression(entity, transactionWriteRequest);
        }
    }

    private T addPutWithIdNullTransactionWriteExpression(T entity, TransactionWriteRequest transactionWriteRequest) {
        transactionWriteRequest.addPut(entity, createIdNullTransactionWriteExpression());
        return entity;
    }

    private T addUpdateWithIdNotNullTransactionWriteExpression(T entity, TransactionWriteRequest transactionWriteRequest) {
        transactionWriteRequest.addUpdate(entity, createIdNotNullTransactionWriteExpression());
        return entity;
    }

    private DynamoDBTransactionWriteExpression createIdNotNullTransactionWriteExpression() {
        return Optional.of(createIdNotNullSearchCriterion())
                .map(Set::of)
                .map(transactionWriteExpressionFromSearchCriteriaConverter::convert)
                .orElseThrow();
    }

    private SearchCriterion createIdNotNullSearchCriterion() {
        return SearchCriterion.builder()
                .field(ID_FIELD)
                .operation(IS_NOT_NULL)
                .build();
    }

    private DynamoDBTransactionWriteExpression createIdNullTransactionWriteExpression() {
        return Optional.of(createIdNullSearchCriterion())
                .map(Set::of)
                .map(transactionWriteExpressionFromSearchCriteriaConverter::convert)
                .orElseThrow();
    }

    private SearchCriterion createIdNullSearchCriterion() {
        return SearchCriterion.builder()
                .field(ID_FIELD)
                .operation(IS_NULL)
                .build();
    }

    private void createConditionCheckForRootConstraintEntity(T entity, Map<ParentalEntity<?, ?>, DynamoDBTransactionWriteExpression> transactionConditionChecks) {
        Optional.of(entity)
                .filter(ParentalEntity.class::isInstance)
                .map(ParentalEntity.class::cast)
                .map(ParentalEntity::getParent)
                .ifPresent(parent -> transactionConditionChecks.putIfAbsent(parent, createIdNotNullTransactionWriteExpression()));
    }

    @Override
    public T findById(String id) {
        return dynamoDBMapper.load(getEntityClass(), id);
    }

    @Override
    public List<T> findAll(Set<SearchCriterion> searchCriteria, Pageable pageable) {
        DynamoDBScanExpression dynamoDBScanExpression = scanExpressionFromSearchCriteriaConverter.convert(searchCriteria);
        Pageable verifiedPageable = createDefaultPageableIfNull(pageable);
        return dynamoDBMapper.scan(getEntityClass(), dynamoDBScanExpression).stream()
                .filter(not(entity -> ROOT_ID.equals(entity.getId())))
                .sorted(createPageableComparator(verifiedPageable))
                .skip((long) verifiedPageable.getPageNumber() * verifiedPageable.getPageSize())
                .limit(verifiedPageable.getPageSize())
                .collect(Collectors.toList());
    }

    private Pageable createDefaultPageableIfNull(Pageable pageable) {
        return Optional.ofNullable(pageable)
                .orElseGet(() -> {
                    int count = getCount();
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
        Comparator<T> comparator = Comparator.comparing(entity -> getFieldValue(fieldName, entity));
        if (isDescending) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    private Comparable getFieldValue(String fieldName, T entity) {
        return ((Comparable) classUtils.getFieldValue(fieldName, entity));
    }

    @Override
    public void deleteById(String id) {
        Optional.of(id)
                .map(Set::of)
                .ifPresent(this::deleteAllById);
    }

    @Override
    public void deleteAllById(Set<String> ids) {
        Set<SearchCriterion> searchCriteria = ids.stream()
                .map(id -> SearchCriterion.builder()
                        .field(ID_FIELD)
                        .value(String.format("%s<%s>", id, PrimitiveType.STRING.name()))
                        .operation(EQUALS)
                        .build())
                .collect(Collectors.toSet());
        Optional.of(searchCriteria)
                .map(this::findAll)
                .ifPresent(this::deleteAll);
    }

    @Override
    public void deleteAll(List<T> entities) {
        TransactionWriteRequest transactionWriteRequest = new TransactionWriteRequest();
        entities.stream()
                .flatMap(this::addRootConstraintEntity)
                .forEach(entity -> transactionWriteRequest.addDelete(entity, createIdNotNullTransactionWriteExpression()));
        executeTransactionWriteRequest(transactionWriteRequest);
    }

    private void executeTransactionWriteRequest(TransactionWriteRequest transactionWriteRequest) {
        Optional.of(transactionWriteRequest)
                .filter(not(writeRequest -> writeRequest.getTransactionWriteOperations().isEmpty()))
                .ifPresent(dynamoDBMapper::transactionWrite);
    }

    @Override
    public Class<T> getEntityClass() {
        return classUtils.getGenericSuperclass(getClass())
                .map(entityClass -> (Class<T>) entityClass)
                .orElseThrow();
    }

    @Override
    public int getCount() {
        return dynamoDBMapper.count(getEntityClass(), new DynamoDBScanExpression());
    }

    @Override
    public List<T> getChildren(String id) {
        SearchCriterion searchCriterion = SearchCriterion.builder()
                .operation(EQUALS)
                .field(PARENT_FIELD)
                .value(String.format("%s<%s>", id, PrimitiveType.STRING.name()))
                .build();
        return Optional.of(searchCriterion)
                .map(Set::of)
                .map(this::findAll)
                .orElseGet(Collections::emptyList);
    }

    @Override
    public List<T> getParentage(String id) {
        List<T> parents = new ArrayList<>();
        Optional.ofNullable(findById(id))
                .filter(ParentalEntity.class::isInstance)
                .filter(parents::add)
                .map(ParentalEntity.class::cast)
                .map(ParentalEntity::getParent)
                .map(Entity::getId)
                .map(String.class::cast)
                .ifPresent(parentId -> parents.addAll(getParentage(parentId)));
        return parents;
    }
}
