package com.pavelshapel.test.spring.boot.starter.layer;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import com.pavelshapel.aws.spring.boot.starter.repository.DynamoDbRepository;
import com.pavelshapel.aws.spring.boot.starter.util.DbHandler;
import com.pavelshapel.core.spring.boot.starter.model.Entity;
import com.pavelshapel.test.spring.boot.starter.container.DynamoDBExtension;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.util.Collections.singletonList;


@SpringBootTest
@ExtendWith(DynamoDBExtension.class)
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractDynamoDbRepositoryTest<ID, T extends Entity<ID>> {
    public static final String ID = "id";

    @Autowired
    private DynamoDbRepository<ID, T> dynamoDbRepository;
    @Autowired
    private DbHandler dynamoDbHandler;

    protected String createTableIfNotExists(String tableName, List<KeySchemaElement> keySchemaElements, List<AttributeDefinition> attributeDefinitions) {
        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(10L, 10L);
        return dynamoDbHandler.createTableIfNotExists(tableName, keySchemaElements, attributeDefinitions, provisionedThroughput);
    }

    protected String createTableIfNotExists(String tableName) {
        List<KeySchemaElement> keySchemaElements = singletonList(new KeySchemaElement(ID, KeyType.HASH));
        List<AttributeDefinition> attributeDefinitions = singletonList(new AttributeDefinition(ID, ScalarAttributeType.S));
        return createTableIfNotExists(tableName, keySchemaElements, attributeDefinitions);
    }

    protected String deleteTableIfExists(String tableName) {
        return dynamoDbHandler.deleteTableIfExists(tableName);
    }

    protected T save(T entity) {
        return dynamoDbHandler.save(entity);
    }

    protected List<T> findAll(Class<T> entityClass) {
        return dynamoDbHandler.findAll(entityClass);
    }

    protected List<DynamoDBMapper.FailedBatch> deleteAll(Class<T> entityClass) {
        return dynamoDbHandler.deleteAll(entityClass);
    }
}