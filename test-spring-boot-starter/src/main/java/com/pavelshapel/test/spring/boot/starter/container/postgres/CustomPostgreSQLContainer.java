package com.pavelshapel.test.spring.boot.starter.container.postgres;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;
import org.testcontainers.containers.PostgreSQLContainer;

public class CustomPostgreSQLContainer extends PostgreSQLContainer<CustomPostgreSQLContainer> {
    private static final String POSTGRES_IMAGE = "postgres:9.6.21";
    private static CustomPostgreSQLContainer container;

    private CustomPostgreSQLContainer() {
        super(POSTGRES_IMAGE);
    }

    public static CustomPostgreSQLContainer getInstance(String initScript) {
        if (container == null) {
            container = new CustomPostgreSQLContainer();
        }

        return StringUtils.hasLength(initScript)
                ? container.withInitScript(initScript)
                : container;
    }

    public static CustomPostgreSQLContainer getInstance() {
        return getInstance(null);
    }

    public static class PostgreSQLInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    String.format("spring.datasource.url=%s", container.getJdbcUrl()),
                    String.format("spring.datasource.username=%s", container.getUsername()),
                    String.format("spring.datasource.password=%s", container.getPassword())
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}