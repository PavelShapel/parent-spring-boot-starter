package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.pavelshapel.aop.spring.boot.starter.annotation.ExceptionWrapped;
import com.pavelshapel.aws.spring.boot.starter.api.service.DynamoDbService;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.util.ClassUtils;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.pavelshapel.core.spring.boot.starter.api.model.Entity.ID_FIELD;
import static com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity.PARENT_FIELD;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExceptionWrapped
public abstract class AbstractDynamoDbService<T extends Entity<String>> implements DynamoDbService<T> {
    public static final Long CAPACITY = 10L;
    @Autowired
    AmazonDynamoDB amazonDynamoDB;
    @Autowired
    DynamoDBMapper dynamoDBMapper;
    @Autowired
    ClassUtils classUtils;

    @Override
    public boolean createTableIfNotExists(List<KeySchemaElement> keySchemaElements,
                                          List<AttributeDefinition> attributeDefinitions,
                                          ProvisionedThroughput provisionedThroughput) {
        CreateTableRequest createTableRequest = new CreateTableRequest()
                .withTableName(getTableName())
                .withProvisionedThroughput(provisionedThroughput)
                .withKeySchema(keySchemaElements)
                .withAttributeDefinitions(attributeDefinitions);
        return createTableAndWaitUntilActive(createTableRequest);
    }

    @SneakyThrows
    private boolean createTableAndWaitUntilActive(CreateTableRequest createTableRequest) {
        boolean isTableCreated = TableUtils.createTableIfNotExists(amazonDynamoDB, createTableRequest);
        if (isTableCreated) {
            TableUtils.waitUntilActive(amazonDynamoDB, createTableRequest.getTableName());
        }
        return isTableCreated;
    }

    @Override
    public boolean createDefaultTableIfNotExists() {
        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(CAPACITY, CAPACITY);
        List<KeySchemaElement> keySchemaElements = new ArrayList<>();
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        if (isEntityIdFieldPresent()) {
            keySchemaElements.add(new KeySchemaElement(ID_FIELD, KeyType.HASH));
            attributeDefinitions.add(new AttributeDefinition(ID_FIELD, ScalarAttributeType.S));
        }
        if (isEntityParentFieldPresent()) {
            keySchemaElements.add(new KeySchemaElement(PARENT_FIELD, KeyType.RANGE));
            attributeDefinitions.add(new AttributeDefinition(PARENT_FIELD, ScalarAttributeType.S));
        }
        return createTableIfNotExists(keySchemaElements, attributeDefinitions, provisionedThroughput);
    }

    @Override
    public boolean deleteTableIfExists() {
        DeleteTableRequest deleteTableRequest = new DeleteTableRequest().withTableName(getTableName());
        return TableUtils.deleteTableIfExists(amazonDynamoDB, deleteTableRequest);
    }

    @Override
    public T save(T entity) {
        dynamoDBMapper.save(entity);
        return entity;
    }


    @Override
    public List<T> saveAll(List<T> entities) {
        dynamoDBMapper.batchSave(entities);
        return entities;
    }

    //    @Override
//    public <ID, T extends Entity<ID>> T findById(Class<T> entityClass, ID id) {
//        return dynamoDBMapper.load(entityClass, id);
//    }
//
//    @Override
//    public <ID, T extends Entity<ID>> List<T> findAll(Class<T> entityClass, Collection<SearchCriterion> searchCriteria, Pageable pageable) {
//        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
//        return dynamoDBMapper.scan(entityClass, scanExpression);
//    }
//
//    public Stream<T> filterStream(Class<T> entityClass, Collection<SearchCriterion> searchCriteria) {
//        searchCriteria.stream()
//                .map
//
//        Stream<SearchCriterion> stream = searchCriteria.stream();
//        for (SearchCriterion searchCriterion : searchCriteria) {
//            stream = stream.filter(entity -> isApplicableEntity(entityClass, searchCriterion));
//        }
//        return stream;
//    }
//
//    private boolean isApplicableEntity(Class<T> entityClass, SearchCriterion searchCriterion) {
//        return getSearchCriterionPredicate(searchCriterion).test(getFieldValue(entityClass, searchCriterion));
//    }
//
//    private Predicate<Comparable<Object>> getSearchCriterionPredicate(SearchCriterion searchCriterion) {
//        return searchCriterion.getOperation().getFunction().apply(searchCriterion.getCastedValue());
//    }
//
//    private Comparable<Object> getFieldValue(Class<T> entityClass, SearchCriterion searchCriterion) {
//        Field field = findField(entityClass, searchCriterion.getField());
//        if (nonNull(field)) {
//            makeAccessible(field);
//            return ((Comparable<Object>) getField(field, entity));
//        } else {
//            throw new IllegalArgumentException(searchCriterion.getField());
//        }
//    }
//
//    @Override
//    public <ID, T extends Entity<ID>> List<DynamoDBMapper.FailedBatch> deleteAll(List<T> entities) {
//        return dynamoDBMapper.batchDelete(entities);
//    }
//
//    @Override
//    public <ID, T extends Entity<ID>> List<DynamoDBMapper.FailedBatch> deleteAll(Class<T> entityClass) {
//        return deleteAll(findAll(entityClass));
//    }
//
    @Override
    public Class<T> getEntityClass() {
        return classUtils.getGenericSuperclass(getClass())
                .map(entityClass -> (Class<T>) entityClass)
                .orElseThrow(UnsupportedOperationException::new);
    }

    @Override
    public String getTableName() {
        return getEntityClass().getSimpleName();
    }

    private boolean isEntityIdFieldPresent() {
        return isEntityFieldExists(ID_FIELD);
    }

    private boolean isEntityParentFieldPresent() {
        return isEntityFieldExists(PARENT_FIELD);
    }

    private boolean isEntityFieldExists(String fieldName) {
        Field field = ReflectionUtils.findField(getEntityClass(), fieldName);
        return Optional.ofNullable(field).isPresent();
    }

}
