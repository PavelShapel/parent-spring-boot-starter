package com.pavelshapel.aws.spring.boot.starter.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDate;

public class LocalDateDynamoDBTypeConverter implements DynamoDBTypeConverter<String, LocalDate> {

    @Override
    public String convert(LocalDate value) {
        return value.toString();
    }

    @Override
    public LocalDate unconvert(String value) {
        return LocalDate.parse(value);
    }
}