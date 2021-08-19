package com.pavelshapel.test.spring.boot.starter.container.postgres;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractPostgreSQLContainer {
    private static final String POSTGRE_SQL_IMAGE = "postgres:9.6.21";

    protected static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER;

    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(POSTGRE_SQL_IMAGE);
        POSTGRE_SQL_CONTAINER.start();
    }

    protected AbstractPostgreSQLContainer() {
    }

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.initialization-mode", () -> "never");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }
}