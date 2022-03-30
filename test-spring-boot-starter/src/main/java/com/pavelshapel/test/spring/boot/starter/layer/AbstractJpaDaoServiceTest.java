package com.pavelshapel.test.spring.boot.starter.layer;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;


@ExtendWith(MockitoExtension.class)
public abstract class AbstractJpaDaoServiceTest<ID, T extends Entity<ID>> {

    @Autowired
    private Specification<T> searchSpecification;
}