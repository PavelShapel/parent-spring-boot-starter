package com.pavelshapel.test.spring.boot.starter.container;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class KafkaExtension implements BeforeAllCallback, AfterAllCallback {
    private static final String DEFAULT_TAG = "6.2.1";
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("confluentinc/cp-kafka");
    private static final String KAFKA_SERVER_PROPERTY_PATH = "spring.kafka.server";
    private static final String KAFKA_CONSUMER_AUTO_OFFSET_RESET_PATH = "spring.kafka.consumer.auto-offset-reset";
    private static final String KAFKA_CONSUMER_GROUP_ID_PATH = "spring.kafka.consumer.group-id";
    private static final String EARLIEST = "earliest";
    private static final String GROUP_ID = "kafka-test";

    private KafkaContainer kafkaContainer;

    @Override
    public void beforeAll(ExtensionContext context) {
        kafkaContainer = new KafkaContainer(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
        kafkaContainer.start();
        System.setProperty(KAFKA_SERVER_PROPERTY_PATH, kafkaContainer.getBootstrapServers());
        System.setProperty(KAFKA_CONSUMER_GROUP_ID_PATH, GROUP_ID);
        System.setProperty(KAFKA_CONSUMER_AUTO_OFFSET_RESET_PATH, EARLIEST);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        kafkaContainer.stop();
    }
}