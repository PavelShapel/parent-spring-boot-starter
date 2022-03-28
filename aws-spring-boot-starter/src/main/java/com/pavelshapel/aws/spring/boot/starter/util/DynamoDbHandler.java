package com.pavelshapel.aws.spring.boot.starter.util;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DynamoDbHandler implements DbHandler {
    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

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
                .orElse(null);
    }

    @Override
    public String createTable(String tableName, List<KeySchemaElement> keySchemaElements, List<AttributeDefinition> attributeDefinitions, ProvisionedThroughput provisionedThroughput) {
        return Optional.of(createDynamoDB())
                .map(dynamoDB -> createTable(tableName, keySchemaElements, attributeDefinitions, provisionedThroughput, dynamoDB))
                .orElse(null);
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
                .orElse(null);
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