package com.pavelshapel.aws.spring.boot.starter.util.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.pavelshapel.aop.spring.boot.starter.log.method.Loggable;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import com.pavelshapel.aws.spring.boot.starter.properties.nested.DynamoDbNestedProperties;
import com.pavelshapel.aws.spring.boot.starter.util.DbHandler;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.pavelshapel.core.spring.boot.starter.api.model.Entity.ID_FIELD;
import static java.util.Collections.singletonList;

@Loggable
public class DynamoDbHandler implements DbHandler {
    public static final Long CAPACITY = 10L;
    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    @Autowired
    private AwsProperties awsProperties;

    @PostConstruct
    private void postConstruct() {
        Optional.ofNullable(awsProperties)
                .map(AwsProperties::getDynamoDb)
                .map(DynamoDbNestedProperties::getTable)
                .ifPresent(this::createDefaultTableIfNotExists);
    }

    @Override
    public List<String> getTableNames() {
        return Optional.of(amazonDynamoDB)
                .map(AmazonDynamoDB::listTables)
                .map(ListTablesResult::getTableNames)
                .orElseGet(Collections::emptyList);
    }

    @Override
    public boolean isTableExists(String tableName) {
        return getTableNames().contains(tableName);
    }

    @Override
    public String createTableIfNotExists(String tableName, List<KeySchemaElement> keySchemaElements, List<AttributeDefinition> attributeDefinitions, ProvisionedThroughput provisionedThroughput) {
        return Optional.of(isTableExists(tableName))
                .filter(Boolean.FALSE::equals)
                .map(unused -> createTable(tableName, keySchemaElements, attributeDefinitions, provisionedThroughput))
                .orElse(tableName);
    }

    @Override
    public String createDefaultTableIfNotExists(String tableName) {
        return Optional.of(isTableExists(tableName))
                .filter(Boolean.FALSE::equals)
                .map(unused -> createDefaultTable(tableName))
                .orElse(tableName);
    }

    @Override
    public String createTable(String tableName, List<KeySchemaElement> keySchemaElements, List<AttributeDefinition> attributeDefinitions, ProvisionedThroughput provisionedThroughput) {
        return Optional.of(createDynamoDB())
                .map(dynamoDB -> createTable(tableName, keySchemaElements, attributeDefinitions, provisionedThroughput, dynamoDB))
                .orElse(null);
    }

    @Override
    public String createDefaultTable(String tableName) {
        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(CAPACITY, CAPACITY);
        List<KeySchemaElement> keySchemaElements = singletonList(new KeySchemaElement(ID_FIELD, KeyType.HASH));
        List<AttributeDefinition> attributeDefinitions = singletonList(new AttributeDefinition(ID_FIELD, ScalarAttributeType.S));
        return createTable(tableName, keySchemaElements, attributeDefinitions, provisionedThroughput);
    }

    @SneakyThrows
    private String createTable(String tableName, List<KeySchemaElement> keySchemaElements, List<AttributeDefinition> attributeDefinitions, ProvisionedThroughput provisionedThroughput, DynamoDB dynamoDB) {
        Table table = dynamoDB.createTable(tableName, keySchemaElements, attributeDefinitions, provisionedThroughput);
        table.waitForActive();
        return table.getTableName();
    }

    @Override
    public String deleteTableIfExists(String tableName) {
        return Optional.of(isTableExists(tableName))
                .filter(Boolean.TRUE::equals)
                .map(unused -> deleteTable(tableName))
                .orElse(tableName);
    }

    @Override
    public String deleteTable(String tableName) {
        return Optional.of(createDynamoDB())
                .map(dynamoDB -> dynamoDB.getTable(tableName))
                .map(this::deleteTable)
                .orElse(null);
    }

    @SneakyThrows
    private String deleteTable(Table table) {
        table.delete();
        table.waitForDelete();
        return table.getTableName();
    }

    @Override
    public <ID, T extends Entity<ID>> T save(T entity) {
        dynamoDBMapper.save(entity);
        return entity;
    }

    @Override
    public <ID, T extends Entity<ID>> T findById(Class<T> entityClass, ID id) {
        return dynamoDBMapper.load(entityClass, id);
    }

    @Override
    public <ID, T extends Entity<ID>> List<T> findAll(Class<T> entityClass) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return dynamoDBMapper.scan(entityClass, scanExpression);
    }

    @Override
    public <ID, T extends Entity<ID>> List<DynamoDBMapper.FailedBatch> deleteAll(List<T> entities) {
        return dynamoDBMapper.batchDelete(entities);
    }

    @Override
    public <ID, T extends Entity<ID>> List<DynamoDBMapper.FailedBatch> deleteAll(Class<T> entityClass) {
        return deleteAll(findAll(entityClass));
    }

    private DynamoDB createDynamoDB() {
        return new DynamoDB(amazonDynamoDB);
    }
}
