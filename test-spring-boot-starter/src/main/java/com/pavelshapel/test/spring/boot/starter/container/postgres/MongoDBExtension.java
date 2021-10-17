package com.pavelshapel.test.spring.boot.starter.container.postgres;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class MongoDBExtension implements BeforeAllCallback, AfterAllCallback {
    private static final String DEFAULT_TAG = "5.0.3";
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("mongo");
    private static final String MONGO_URI_PROPERTY_PATH = "spring.data.mongodb.uri";

    private MongoDBContainer mongoDBContainer;

    @Override
    public void beforeAll(ExtensionContext context) {
        mongoDBContainer = new MongoDBContainer(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
        mongoDBContainer.start();
        System.setProperty(MONGO_URI_PROPERTY_PATH, mongoDBContainer.getReplicaSetUrl());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        mongoDBContainer.stop();
    }
}