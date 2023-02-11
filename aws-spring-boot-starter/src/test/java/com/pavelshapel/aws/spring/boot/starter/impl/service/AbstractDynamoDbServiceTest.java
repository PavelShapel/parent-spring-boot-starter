package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.pavelshapel.aws.spring.boot.starter.AbstractTest;
import com.pavelshapel.aws.spring.boot.starter.api.service.DynamoDbService;
import com.pavelshapel.aws.spring.boot.starter.config.User;
import com.pavelshapel.aws.spring.boot.starter.provider.OneStringProvider;
import com.pavelshapel.test.spring.boot.starter.container.DynamoDbAwsExtension;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(DynamoDbAwsExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class AbstractDynamoDbServiceTest extends AbstractTest {
    @Autowired
    DynamoDbService<User> userDynamoDbService;
    @Autowired
    AmazonDynamoDB amazonDynamoDB;

    @BeforeEach
    void setUp() {
        userDynamoDbService.createDefaultTableIfNotExists();
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void save_WithValidParameter_ShouldSaveAndReturnEntity(String userName) {
        User user = new User();
        user.setName(userName);

        User result = userDynamoDbService.save(user);

        assertThat(result)
                .hasFieldOrPropertyWithValue("name", userName)
                .extracting(User::getId)
                .isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void saveAll_WithValidParameter_ShouldSaveAndReturnEntity(String userName) {
        User user = new User();
        user.setName(userName);

        List<User> result = userDynamoDbService.saveAll(List.of(user));

        assertThat(result)
                .asList()
                .contains(user)
                .first()
                .extracting(User.class::cast)
                .extracting(User::getId)
                .isNotNull();
    }
}