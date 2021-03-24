package com.pavelshapel.test.spring.boot.starter.container.postgres;

import org.slf4j.event.Level;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Objects;

public class CustomPostgreSQLContainer extends PostgreSQLContainer<CustomPostgreSQLContainer> {
    private static final String POSTGRES_IMAGE = "postgres:9.6.21";
    private static CustomPostgreSQLContainer container;

    private CustomPostgreSQLContainer() {
        super(POSTGRES_IMAGE);
    }

    public static CustomPostgreSQLContainer getInstance(Level loggerLevel) {
        container = new CustomPostgreSQLContainer();

        return setLoggerLevel(loggerLevel);
    }

    private static CustomPostgreSQLContainer setLoggerLevel(Level loggerLevel) {
        final String key = "loggerLevel";
        return Objects.isNull(loggerLevel)
                ? container.withUrlParam(key, Level.DEBUG.toString())
                : container.withUrlParam(key, loggerLevel.toString());
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