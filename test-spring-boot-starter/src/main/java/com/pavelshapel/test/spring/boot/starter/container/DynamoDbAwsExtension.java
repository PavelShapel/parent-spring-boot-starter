package com.pavelshapel.test.spring.boot.starter.container;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;

public class DynamoDbAwsExtension extends AbstractAwsExtension {
    public DynamoDbAwsExtension() {
        super(DYNAMODB);
    }
}