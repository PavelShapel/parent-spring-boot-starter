package com.pavelshapel.test.spring.boot.starter.container;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractAwsExtension implements BeforeAllCallback, AfterAllCallback {
    private static final String DEFAULT_TAG = "0.11.3";
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("localstack/localstack");
    private static final String AWS_PATH = "spring.aws";
    private static final String AWS_ACCESS_KEY_PATH = String.format("%s.accessKey", AWS_PATH);
    private static final String AWS_SECRET_KEY_PATH = String.format("%s.secretKey", AWS_PATH);
    private static final String AWS_REGION_PATH = String.format("%s.region", AWS_PATH);
    private final LocalStackContainer.Service awsService;
    private final String awsClientEnabledPath;
    private final String awsClientEndpointPath;
    private final LocalStackContainer localStackContainer;

    protected AbstractAwsExtension(LocalStackContainer.Service awsService) {
        this.awsService = awsService;
        String awsClientPath = String.format("%s.%s", AWS_PATH, awsService.getName());
        this.awsClientEnabledPath = String.format("%s.enabled", awsClientPath);
        this.awsClientEndpointPath = String.format("%s.endpoint", awsClientPath);
        this.localStackContainer = new LocalStackContainer(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        localStackContainer.withServices(awsService);
        localStackContainer.start();
        System.setProperty(awsClientEnabledPath, Boolean.TRUE.toString());
        System.setProperty(AWS_ACCESS_KEY_PATH, localStackContainer.getAccessKey());
        System.setProperty(AWS_SECRET_KEY_PATH, localStackContainer.getSecretKey());
        System.setProperty(AWS_REGION_PATH, localStackContainer.getRegion());
        System.setProperty(awsClientEndpointPath, localStackContainer.getEndpointOverride(awsService).toString());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        localStackContainer.stop();
    }
}