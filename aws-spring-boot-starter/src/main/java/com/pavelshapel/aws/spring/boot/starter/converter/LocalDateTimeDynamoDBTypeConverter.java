package com.pavelshapel.aws.spring.boot.starter.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDateTime;

public class LocalDateTimeDynamoDBTypeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {

    @Override
    public String convert(LocalDateTime value) {
        return value.toString();
    }

    @Override
    public LocalDateTime unconvert(String value) {
        return LocalDateTime.parse(value);
    }
}