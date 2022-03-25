package com.pavelshapel.aws.spring.boot.starter.util;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;

import java.util.List;

public interface DbHandler {
    List<String> getTableNames();

    boolean isTableExists(String tableName);

    String createTableIfNotExists(String tableName, List<KeySchemaElement> keySchemaElements, List<AttributeDefinition> attributeDefinitions, ProvisionedThroughput provisionedThroughput);

    String createTable(String tableName, List<KeySchemaElement> keySchemaElements, List<AttributeDefinition> attributeDefinitions, ProvisionedThroughput provisionedThroughput);

    String deleteTableIfExists(String tableName);

    String deleteTable(String tableName);

    <ID, T extends Entity<ID>> T save(T entity);

    <ID, T extends Entity<ID>> T findById(Class<T> targetClass, ID id);

    <ID, T extends Entity<ID>> List<T> findAll(Class<T> entityClass);

    <ID, T extends Entity<ID>> List<DynamoDBMapper.FailedBatch> deleteAll(List<T> entities);

    <ID, T extends Entity<ID>> List<DynamoDBMapper.FailedBatch> deleteAll(Class<T> entityClass);
}
