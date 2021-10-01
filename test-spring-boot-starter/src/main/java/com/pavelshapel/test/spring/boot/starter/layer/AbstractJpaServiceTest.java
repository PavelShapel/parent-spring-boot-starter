package com.pavelshapel.test.spring.boot.starter.layer;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchSpecification;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;


@ExtendWith(MockitoExtension.class)
public abstract class AbstractJpaServiceTest<T extends AbstractEntity> implements MockSearchCriteria{

    @Autowired
    private SearchSpecification<T> searchSpecification;
}