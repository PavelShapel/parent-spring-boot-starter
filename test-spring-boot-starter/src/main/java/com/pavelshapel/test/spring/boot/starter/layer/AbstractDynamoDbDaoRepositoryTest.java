package com.pavelshapel.test.spring.boot.starter.layer;

import com.pavelshapel.aws.spring.boot.starter.api.service.DbHandler;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.jpa.spring.boot.starter.repository.DaoRepository;
import com.pavelshapel.test.spring.boot.starter.annotation.SpringBootTestProfileTest;
import com.pavelshapel.test.spring.boot.starter.container.DynamoDBExtension;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;


@SpringBootTestProfileTest
@ExtendWith(DynamoDBExtension.class)
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractDynamoDbDaoRepositoryTest<ID, T extends Entity<ID>> {
    public static final String ID = "id";

    @Autowired
    private DaoRepository<ID, T> daoRepository;
    @Autowired
    private DbHandler dynamoDbHandler;
}