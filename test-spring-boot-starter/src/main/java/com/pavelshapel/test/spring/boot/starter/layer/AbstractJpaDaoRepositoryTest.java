package com.pavelshapel.test.spring.boot.starter.layer;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.jpa.spring.boot.starter.JpaStarterAutoConfiguration;
import com.pavelshapel.jpa.spring.boot.starter.repository.DaoRepository;
import com.pavelshapel.jpa.spring.boot.starter.service.search.AbstractSearchSpecification;
import com.pavelshapel.test.spring.boot.starter.container.PostgreSQLExtension;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


@DataJpaTest
@Import(JpaStarterAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(PostgreSQLExtension.class)
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractJpaDaoRepositoryTest<ID, T extends Entity<ID>> {
    @Autowired
    private DaoRepository<ID, T> daoRepository;
    @Autowired
    private AbstractSearchSpecification<T> searchSpecification;
    @Autowired
    private TestEntityManager testEntityManager;

    protected T save(T entity) {
        return testEntityManager.persistFlushFind(entity);
    }
}