package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.pavelshapel.aws.spring.boot.starter.api.service.ScanExpressionFromSearchCriteriaConverter;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import org.springframework.lang.Nullable;

import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

public class DynamoDBScanExpressionFromSearchCriteriaConverter implements ScanExpressionFromSearchCriteriaConverter {
    @Override
    public DynamoDBScanExpression convert(@Nullable Set<SearchCriterion> searchCriteria) {
        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();
        if (!isEmpty(searchCriteria)) {
            dynamoDBScanExpression
                    .withFilterExpression(updateExpression(searchCriteria))
                    .withExpressionAttributeNames(updateExpressionAttributeNames(searchCriteria))
                    .withExpressionAttributeValues(updateExpressionAttributeValues(searchCriteria));
        }
        return dynamoDBScanExpression;
    }
}
