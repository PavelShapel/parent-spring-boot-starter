package com.pavelshapel.test.spring.boot.starter.layer;

import com.pavelshapel.jpa.spring.boot.starter.JpaAuditingConfiguration;
import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.jpa.spring.boot.starter.repository.AbstractJpaRepository;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchSpecification;
import com.pavelshapel.test.spring.boot.starter.container.postgres.PostgreSQLExtension;
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
@Import(JpaAuditingConfiguration.class)
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(PostgreSQLExtension.class)
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractJpaRepositoryTest<T extends AbstractEntity> {

    @Autowired
    private AbstractJpaRepository<T> jpaRepository;

    @Autowired
    private SearchSpecification<T> searchSpecification;

    @Autowired
    private TestEntityManager testEntityManager;

    protected T saveAndRetrieve(T entity) {
        return testEntityManager.persistFlushFind(entity);
    }
}