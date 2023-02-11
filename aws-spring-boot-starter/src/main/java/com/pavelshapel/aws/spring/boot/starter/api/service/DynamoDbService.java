package com.pavelshapel.aws.spring.boot.starter.api.service;

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;

import java.util.List;

public interface DynamoDbService<T extends Entity<String>> {
    boolean createTableIfNotExists(List<KeySchemaElement> keySchemaElements, List<AttributeDefinition> attributeDefinitions, ProvisionedThroughput provisionedThroughput);

    boolean createDefaultTableIfNotExists();

    boolean deleteTableIfExists();

    T save(T entity);

    List<T> saveAll(List<T> entities);

//    <ID, T extends Entity<ID>> T findById(Class<T> targetClass, ID id);
//
//    <ID, T extends Entity<ID>> List<T> findAll(Class<T> entityClass);
//
//    <ID, T extends Entity<ID>> List<DynamoDBMapper.FailedBatch> deleteAll(List<T> entities);
//
//    <ID, T extends Entity<ID>> List<DynamoDBMapper.FailedBatch> deleteAll(Class<T> entityClass);

    Class<T> getEntityClass();

    String getTableName();
}
