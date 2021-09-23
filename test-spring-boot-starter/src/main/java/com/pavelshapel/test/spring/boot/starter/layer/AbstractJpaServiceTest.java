package com.pavelshapel.test.spring.boot.starter.layer;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchSpecification;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Getter(AccessLevel.PROTECTED)
@ExtendWith(MockitoExtension.class)
public abstract class AbstractJpaServiceTest<T extends AbstractEntity> {

    @Autowired
    private SearchSpecification<T> searchSpecification;
}