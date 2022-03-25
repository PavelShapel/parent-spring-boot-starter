package com.pavelshapel.aws.spring.boot.starter.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.pavelshapel.aws.spring.boot.starter.service.DynamoDbService;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public abstract class AbstractParentalDynamoDBTypeConverter<ID, T extends ParentalEntity<ID, T>> implements DynamoDBTypeConverter<ID, T> {
    @Autowired
    private DynamoDbService<ID, T> dynamoDbService;

    @Override
    public ID convert(T location) {
        System.out.println("convert");
        System.out.println(dynamoDbService.toString());
        return Optional.ofNullable(location)
                .map(Entity::getId)
                .orElse(null);
    }

    @Override
    public T unconvert(ID id) {
        System.out.println("unconvert");
        System.out.println(dynamoDbService);
        System.out.println(dynamoDbService.toString());
        return Optional.ofNullable(id)
                .map(dynamoDbService::findById)
                .orElse(null);
    }
}