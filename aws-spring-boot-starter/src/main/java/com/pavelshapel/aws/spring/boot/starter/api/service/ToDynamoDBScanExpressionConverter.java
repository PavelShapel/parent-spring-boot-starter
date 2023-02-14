package com.pavelshapel.aws.spring.boot.starter.api.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;

public interface ToDynamoDBScanExpressionConverter<T extends Entity<String>> extends Converter<Set<SearchCriterion>, DynamoDBQueryExpression<T>> {
}
