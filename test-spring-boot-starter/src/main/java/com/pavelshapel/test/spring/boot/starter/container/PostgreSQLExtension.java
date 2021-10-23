package com.pavelshapel.test.spring.boot.starter.container;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgreSQLExtension implements BeforeAllCallback, AfterAllCallback {
    private static final String DEFAULT_TAG = "9.6.12";
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("postgres");

    private PostgreSQLContainer<?> postgreSQLContainer;

    @Override
    public void beforeAll(ExtensionContext context) {
        postgreSQLContainer = new PostgreSQLContainer<>(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
        postgreSQLContainer.start();
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
        System.setProperty("spring.datasource.initialization-mode", "never");
        System.setProperty("spring.jpa.hibernate.ddl-auto", "none");
        System.setProperty("spring.liquibase.enabled", "true");
        System.setProperty("spring.liquibase.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.liquibase.user", postgreSQLContainer.getUsername());
        System.setProperty("spring.liquibase.password", postgreSQLContainer.getPassword());
        System.setProperty("spring.liquibase.change-log", "classpath:db/changelog/db.changelog-master.yml");
    }

    @Override
    public void afterAll(ExtensionContext context) {
        postgreSQLContainer.stop();
    }
}