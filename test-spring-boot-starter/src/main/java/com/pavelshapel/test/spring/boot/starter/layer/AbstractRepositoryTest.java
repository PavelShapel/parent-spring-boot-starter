package com.pavelshapel.test.spring.boot.starter.layer;

import com.pavelshapel.jpa.spring.boot.starter.JpaAuditingConfiguration;
import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.jpa.spring.boot.starter.repository.AbstractJpaRepository;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchCriteria;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchOperation;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchSpecification;
import com.pavelshapel.test.spring.boot.starter.container.postgres.PostgreSQLExtension;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


@DataJpaTest
@ContextConfiguration(classes = {
        JpaAuditingConfiguration.class
})
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(PostgreSQLExtension.class)
@Getter
public abstract class AbstractRepositoryTest<T extends AbstractEntity> {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AbstractJpaRepository<T> jpaRepository;

    @Autowired
    private SearchSpecification<T> searchSpecification;

    protected T saveAndRetrieve(T entity) {
        return testEntityManager.persistFlushFind(entity);
    }

    protected SearchCriteria getSearchCriteria(String value, String field, SearchOperation searchOperation) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setField(field);
        searchCriteria.setValue(value);
        searchCriteria.setOperation(searchOperation);
        return searchCriteria;
    }
}