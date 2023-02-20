package com.pavelshapel.aws.spring.boot.starter.api.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

public interface ScanExpressionFromSearchCriteriaConverter extends ExpressionFromSearchCriteriaConverter<DynamoDBScanExpression> {
}
