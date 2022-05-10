package com.pavelshapel.test.spring.boot.starter.container;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.dynamodb.DynaliteContainer;
import org.testcontainers.utility.DockerImageName;

public class DynamoDBExtension implements BeforeAllCallback, AfterAllCallback {
    private static final String DEFAULT_TAG = "v1.2.1-1";
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("quay.io/testcontainers/dynalite");
    private static final String AWS_PROFILE_PATH = "spring.aws.profile";
    private static final String DYNAMO_DB = "dynamodb";
    private static final String DEFAULT = "default";
    private static final String AWS_DYNAMO_DB_PATH = String.format("spring.aws.%s", DYNAMO_DB);
    private static final String AWS_DYNAMO_DB_ENABLED_PATH = String.format("%s.enabled", AWS_DYNAMO_DB_PATH);
    private static final String AWS_DYNAMO_DB_ENDPOINT_PATH = String.format("%s.endpoint", AWS_DYNAMO_DB_PATH);
    private DynaliteContainer dynamoDBContainer;

    @Override
    public void beforeAll(ExtensionContext context) {
        dynamoDBContainer = new DynaliteContainer(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
        dynamoDBContainer.start();
        System.setProperty(AWS_PROFILE_PATH, DEFAULT);
        System.setProperty(AWS_DYNAMO_DB_ENABLED_PATH, Boolean.TRUE.toString());
        System.setProperty(AWS_DYNAMO_DB_ENDPOINT_PATH, dynamoDBContainer.getEndpointConfiguration().getServiceEndpoint());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        dynamoDBContainer.stop();
    }
}