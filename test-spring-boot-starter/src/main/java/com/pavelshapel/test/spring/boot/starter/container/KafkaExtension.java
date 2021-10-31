package com.pavelshapel.test.spring.boot.starter.container;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class KafkaExtension implements BeforeAllCallback, AfterAllCallback {
    private static final String DEFAULT_TAG = "6.2.1";
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("confluentinc/cp-kafka");
    private static final String KAFKA_SERVER_PROPERTY_PATH = "kafka.server";

    private KafkaContainer kafkaContainer;

    @Override
    public void beforeAll(ExtensionContext context) {
        kafkaContainer = new KafkaContainer(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
        kafkaContainer.start();
        System.setProperty(KAFKA_SERVER_PROPERTY_PATH, kafkaContainer.getBootstrapServers());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        kafkaContainer.stop();
    }
}