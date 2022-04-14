package com.pavelshapel.aws.spring.boot.starter.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity;
import com.pavelshapel.core.spring.boot.starter.api.service.DaoService;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;

import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.Optional;

@NoArgsConstructor
public abstract class AbstractParentalDynamoDBTypeConverter<ID, T extends ParentalEntity<ID, T>> implements DynamoDBTypeConverter<ID, T> {
    private static DaoService staticDaoService;

    @SneakyThrows
    protected AbstractParentalDynamoDBTypeConverter(DaoService<ID, T> daoService) {
        staticDaoService = daoService;
        Arrays.stream(getClass().getConstructors())
                .map(Executable::getParameters)
                .map(parameters -> parameters.length)
                .filter(length -> length.equals(0))
                .findFirst()
                .orElseThrow(this::createNoArgumentsConstructorMustBeImplementedException);
    }

    private NotImplementedException createNoArgumentsConstructorMustBeImplementedException() {
        String message = String.format("the constructor for class [%s] with no arguments must be implemented", getClass().getSimpleName());
        return new NotImplementedException(message);
    }

    @Override
    public ID convert(T location) {
        return Optional.ofNullable(location)
                .map(Entity::getId)
                .orElse(null);
    }

    @Override
    public T unconvert(ID id) {
        return Optional.ofNullable(id)
                .map(notNullId -> staticDaoService.findById(notNullId))
                .map(entity -> (T) entity)
                .orElse(null);
    }
}