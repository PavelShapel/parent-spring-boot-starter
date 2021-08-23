package com.pavelshapel.test.spring.boot.starter.layer;

import com.pavelshapel.jpa.spring.boot.starter.JpaAuditingConfiguration;
import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.jpa.spring.boot.starter.repository.AbstractJpaRepository;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchCriteria;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchOperation;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchSpecification;
import com.pavelshapel.test.spring.boot.starter.annotation.StartPostgreSQLContainer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;


@DataJpaTest
@ContextConfiguration(classes = {
        JpaAuditingConfiguration.class
})
@StartPostgreSQLContainer
@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractRepositoryTest<T extends AbstractEntity> {

    private final AbstractJpaRepository<T> jpaRepository;
    private final SearchSpecification<T> searchSpecification;

    @Autowired
    private TestEntityManager testEntityManager;

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