package com.pavelshapel.aws.spring.boot.starter.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.pavelshapel.aws.spring.boot.starter.api.service.DynamoDbService;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;

import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractParentalDynamoDBTypeConverter<T extends ParentalEntity<String, T>> implements DynamoDBTypeConverter<String, T> {
    private static DynamoDbService<?> staticDynamoDBService;

    protected AbstractParentalDynamoDBTypeConverter(DynamoDbService<T> dynamoDbService) {
        staticDynamoDBService = dynamoDbService;
        boolean isNoArgsConstructorNotExists = Arrays.stream(getClass().getConstructors())
                .map(Executable::getParameters)
                .map(parameters -> parameters.length)
                .noneMatch(Integer.valueOf(0)::equals);
        if (isNoArgsConstructorNotExists) {
            throw noArgsConstructorMustBeImplementedException();
        }
    }

    private NotImplementedException noArgsConstructorMustBeImplementedException() {
        String message = String.format("the constructor for class [%s] with no arguments must be implemented", getClass().getSimpleName());
        return new NotImplementedException(message);
    }

    @Override
    public String convert(T entity) {
        return Optional.ofNullable(entity)
                .map(Entity::getId)
                .orElse(null);
    }

    @Override
    public T unconvert(String id) {
        return (T) Optional.ofNullable(id)
                .map(notNullId -> staticDynamoDBService.findById(notNullId))
                .orElse(null);
    }
}