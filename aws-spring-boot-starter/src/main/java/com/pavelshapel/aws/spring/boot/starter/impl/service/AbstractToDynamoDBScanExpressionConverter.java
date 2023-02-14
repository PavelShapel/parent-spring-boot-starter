package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.pavelshapel.aws.spring.boot.starter.api.service.ToDynamoDBScanExpressionConverter;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

public abstract class AbstractToDynamoDBScanExpressionConverter implements ToDynamoDBScanExpressionConverter {
    @Override
    public DynamoDBScanExpression convert(@Nullable Set<SearchCriterion> searchCriteria) {
        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();
        if (!isEmpty(searchCriteria)) {
            dynamoDBScanExpression
                    .withFilterExpression(updateFilterExpression(searchCriteria))
                    .withExpressionAttributeValues(updateExpressionAttributeValues(searchCriteria));
        }
        return dynamoDBScanExpression;
    }

    private String updateFilterExpression(Set<SearchCriterion> searchCriteria) {
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
