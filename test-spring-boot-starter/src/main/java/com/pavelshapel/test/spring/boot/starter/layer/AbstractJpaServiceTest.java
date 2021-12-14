package com.pavelshapel.test.spring.boot.starter.layer;

import com.pavelshapel.jpa.spring.boot.starter.entity.rds.AbstractEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;


@ExtendWith(MockitoExtension.class)
public abstract class AbstractJpaServiceTest<T extends AbstractEntity> {

    @Autowired
    private Specification<T> searchSpecification;
}