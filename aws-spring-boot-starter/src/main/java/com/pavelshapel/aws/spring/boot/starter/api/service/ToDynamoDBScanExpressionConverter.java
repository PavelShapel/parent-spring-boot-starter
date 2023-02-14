package com.pavelshapel.aws.spring.boot.starter.api.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;

public interface ToDynamoDBScanExpressionConverter extends Converter<Set<SearchCriterion>, DynamoDBScanExpression> {
}
