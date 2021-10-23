package com.pavelshapel.test.spring.boot.starter.container;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

public class ElasticsearchExtension implements BeforeAllCallback, AfterAllCallback {
    private static final String DEFAULT_TAG = "7.15.1";
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch");
    private static final String ELASTICSEARCH_SERVER = "elasticsearch.server";
    private static final String X_PACK_SECURITY_ENABLED = "xpack.security.enabled";

    private ElasticsearchContainer elasticsearchContainer;

    @Override
    public void beforeAll(ExtensionContext context) {
        elasticsearchContainer = new ElasticsearchContainer(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
        elasticsearchContainer.addEnv(X_PACK_SECURITY_ENABLED, Boolean.FALSE.toString());
        elasticsearchContainer.start();
        System.setProperty(ELASTICSEARCH_SERVER, elasticsearchContainer.getHttpHostAddress());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        elasticsearchContainer.stop();
    }
}