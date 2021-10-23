package com.pavelshapel.test.spring.boot.starter.container;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

public class ElasticsearchExtension implements BeforeAllCallback, AfterAllCallback {
    private static final String ELASTICSEARCH = "elasticsearch";
    private static final String DEFAULT_TAG = "6.8.20";
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("elasticsearch").asCompatibleSubstituteFor("docker.elastic.co/elasticsearch/elasticsearch");
    private static final String ELASTICSEARCH_SERVER = "elasticsearch.server";
    private static final String DISCOVERY_TYPE = "discovery.type";
    private static final String SINGLE_NODE = "single-node";
    private static final String CLUSTER_NAME = "cluster.name";

    private ElasticsearchContainer elasticsearchContainer;

    @Override
    public void beforeAll(ExtensionContext context) {
        elasticsearchContainer = new ElasticsearchContainer(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
        elasticsearchContainer.addEnv(DISCOVERY_TYPE, SINGLE_NODE);
        elasticsearchContainer.addEnv(CLUSTER_NAME, ELASTICSEARCH);
        elasticsearchContainer.start();
        System.setProperty(ELASTICSEARCH_SERVER, elasticsearchContainer.getHttpHostAddress());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        elasticsearchContainer.stop();
    }
}