package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.pavelshapel.aws.spring.boot.starter.api.service.ToDynamoDBScanExpressionConverter;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

public abstract class AbstractToDynamoDBScanExpressionConverter<T extends Entity<String>> implements ToDynamoDBScanExpressionConverter<T> {
    @Override
    public DynamoDBQueryExpression<T> convert(@Nullable Set<SearchCriterion> searchCriteria) {
        DynamoDBQueryExpression<T> dynamoDBQueryExpression = new DynamoDBQueryExpression<>();
        if (!isEmpty(searchCriteria)) {
            dynamoDBQueryExpression
                    .withKeyConditionExpression(updateKeyConditionExpression(searchCriteria))
                    .withExpressionAttributeValues(updateExpressionAttributeValues(searchCriteria));
        }
        return dynamoDBQueryExpression;
    }

    private String updateKeyConditionExpression(Set<SearchCriterion> searchCriteria) {
        return searchCriteria.stream()
                .map(searchCriterion -> String.format(searchCriterion.getOperation().getSearchOperationPattern(), searchCriterion.getField()))
                .collect(Collectors.joining(" and "));
    }

    private Map<String, AttributeValue> updateExpressionAttributeValues(Set<SearchCriterion> searchCriteria) {
        return searchCriteria.stream()
                .collect(Collectors.toMap(searchCriterion -> String.format(":%s", searchCriterion.getField()),
                        searchCriterion -> ItemUtils.toAttributeValue(searchCriterion.getCastedValue())));
    }
}
