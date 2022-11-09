package com.pavelshapel.test.spring.boot.starter.container;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

public class AwsS3Extension implements BeforeAllCallback, AfterAllCallback {
    private static final String DEFAULT_TAG = "0.11.3";
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("localstack/localstack");
    private static final String AWS_PATH = "spring.aws";
    private static final String AWS_ACCESS_KEY_PATH = String.format("%s.accessKey", AWS_PATH);
    private static final String AWS_SECRET_KEY_PATH = String.format("%s.secretKey", AWS_PATH);
    private static final String AWS_REGION_PATH = String.format("%s.region", AWS_PATH);
    private static final LocalStackContainer.Service AWS_CLIENT = S3;
    private static final String AWS_CLIENT_PATH = String.format("%s.%s", AWS_PATH, AWS_CLIENT.getName());
    private static final String AWS_CLIENT_ENABLED_PATH = String.format("%s.enabled", AWS_CLIENT_PATH);
    private static final String AWS_CLIENT_ENDPOINT_PATH = String.format("%s.endpoint", AWS_CLIENT_PATH);
    private LocalStackContainer localStackContainer;

    @Override
    public void beforeAll(ExtensionContext context) {
        localStackContainer = new LocalStackContainer(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
        localStackContainer.withServices(AWS_CLIENT);
        localStackContainer.start();
        System.setProperty(AWS_CLIENT_ENABLED_PATH, Boolean.TRUE.toString());
        System.setProperty(AWS_ACCESS_KEY_PATH, localStackContainer.getAccessKey());
        System.setProperty(AWS_SECRET_KEY_PATH, localStackContainer.getSecretKey());
        System.setProperty(AWS_REGION_PATH, localStackContainer.getRegion());
        System.setProperty(AWS_CLIENT_ENDPOINT_PATH, localStackContainer.getEndpointOverride(S3).toString());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        localStackContainer.stop();
    }
}