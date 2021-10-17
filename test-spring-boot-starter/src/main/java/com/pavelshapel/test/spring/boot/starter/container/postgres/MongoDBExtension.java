package com.pavelshapel.test.spring.boot.starter.container.postgres;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class MongoDBExtension implements BeforeAllCallback, AfterAllCallback {
    private static final String DEFAULT_TAG = "5.0.3";
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("mongo");
    private static final String MONGO_INITDB_DATABASE = "test";
    private static final String MONGO_INITDB_ROOT_USERNAME = "admin";
    private static final String MONGO_INITDB_ROOT_PASSWORD = "secret";
    private static final String MONGO_URI_TEMPLATE = "mongodb://%s:%s@%s:%d/%s";
    private static final String MONGO_URI_PROPERTY_PATH = "spring.data.mongodb.uri";

    private MongoDBContainer mongoDBContainer;

    @Override
    public void beforeAll(ExtensionContext context) {
        mongoDBContainer = new MongoDBContainer(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG))
                .withEnv("MONGO_INITDB_ROOT_USERNAME", MONGO_INITDB_ROOT_USERNAME)
                .withEnv("MONGO_INITDB_ROOT_PASSWORD", MONGO_INITDB_ROOT_PASSWORD);
        mongoDBContainer.start();
        String uri = String.format(
                MONGO_URI_TEMPLATE,
                MONGO_INITDB_ROOT_USERNAME,
                MONGO_INITDB_ROOT_PASSWORD,
                mongoDBContainer.getHost(),
                mongoDBContainer.getFirstMappedPort(),
                MONGO_INITDB_DATABASE);
        System.setProperty(MONGO_URI_PROPERTY_PATH, uri);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        mongoDBContainer.stop();
    }
}