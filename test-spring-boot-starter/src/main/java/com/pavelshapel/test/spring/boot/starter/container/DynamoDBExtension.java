package com.pavelshapel.test.spring.boot.starter.container;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.dynamodb.DynaliteContainer;
import org.testcontainers.utility.DockerImageName;

public class DynamoDBExtension implements BeforeAllCallback, AfterAllCallback {
    private static final String DEFAULT_TAG = "v1.2.1-1";
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("quay.io/testcontainers/dynalite");

    private DynaliteContainer dynamoDBContainer;

    @Override
    public void beforeAll(ExtensionContext context) {
        dynamoDBContainer = new DynaliteContainer(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
        dynamoDBContainer.start();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        dynamoDBContainer.stop();
    }
}