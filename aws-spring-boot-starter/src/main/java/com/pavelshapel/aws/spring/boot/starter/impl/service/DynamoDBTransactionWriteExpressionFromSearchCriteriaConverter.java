package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTransactionWriteExpression;
import com.pavelshapel.aws.spring.boot.starter.api.service.TransactionWriteExpressionFromSearchCriteriaConverter;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import org.springframework.lang.Nullable;

import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

public class DynamoDBTransactionWriteExpressionFromSearchCriteriaConverter implements TransactionWriteExpressionFromSearchCriteriaConverter {
    @Override
    public DynamoDBTransactionWriteExpression convert(@Nullable Set<SearchCriterion> searchCriteria) {
        DynamoDBTransactionWriteExpression dynamoDBTransactionWriteExpression = new DynamoDBTransactionWriteExpression();
        if (!isEmpty(searchCriteria)) {
            dynamoDBTransactionWriteExpression
                    .withConditionExpression(updateExpression(searchCriteria))
                    .withExpressionAttributeNames(updateExpressionAttributeNames(searchCriteria))
                    .withExpressionAttributeValues(updateExpressionAttributeValues(searchCriteria));
        }
        return dynamoDBTransactionWriteExpression;
    }
}
