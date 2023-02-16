package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.pavelshapel.aws.spring.boot.starter.AbstractTest;
import com.pavelshapel.aws.spring.boot.starter.api.service.DynamoDbService;
import com.pavelshapel.aws.spring.boot.starter.context.User;
import com.pavelshapel.aws.spring.boot.starter.provider.OneStringProvider;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchOperation;
import com.pavelshapel.test.spring.boot.starter.container.DynamoDbAwsExtension;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.pavelshapel.core.spring.boot.starter.api.model.Entity.ID_FIELD;
import static com.pavelshapel.core.spring.boot.starter.api.model.Named.NAME_FIELD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(DynamoDbAwsExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class AbstractDynamoDbServiceTest extends AbstractTest {
    private static final long CAPACITY = 10L;
    @SpyBean
    DynamoDBMapper dynamoDBMapper;
    @Autowired
    DynamoDbService<User> userDynamoDbService;
    @Autowired
    AmazonDynamoDB amazonDynamoDB;

    @BeforeEach
    void setUp() {
        createDefaultTableIfNotExists();
    }

    @AfterEach
    void tearDown() {
        List<User> all = userDynamoDbService.findAll(null, null);
        all.stream()
                .map(User::getId)
                .forEach(id -> userDynamoDbService.deleteById(id));
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void save_WithValidParameter_ShouldSaveAndReturnEntity(String userName) {
        User user = createUser(userName);

        User result = userDynamoDbService.save(user);

        assertThat(result)
                .hasFieldOrPropertyWithValue("name", userName)
                .extracting(User::getId)
                .isNotNull();
    }

    @ParameterizedTest
    @NullSource
    void save_WithNullParameter_ShouldThrowException(User user) {
        assertThatThrownBy(() -> userDynamoDbService.save(user))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void saveAll_WithValidParameter_ShouldSaveAndReturnEntities(String userName) {
        User user = createUser(userName);

        Collection<User> result = userDynamoDbService.saveAll(List.of(user));

        assertThat(result)
                .asList()
                .contains(user)
                .first()
                .extracting(User.class::cast)
                .extracting(User::getId)
                .isNotNull();
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    void saveAll_WithNullOrEmptyParameter_ShouldSaveAndReturnEntity(List<User> users) {
        userDynamoDbService.saveAll(users);

        verifyNoInteractions(dynamoDBMapper);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void findById_WithValidParameter_ShouldReturnEntity(String userName) {
        User user = createUser(userName);
        User savedUser = userDynamoDbService.save(user);

        User result = userDynamoDbService.findById(savedUser.getId());

        assertThat(result).isEqualTo(savedUser);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void findById_WithNullParameter_ShouldThrowException(String id) {
        assertThatThrownBy(() -> userDynamoDbService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findAll_StartsWith_ShouldReturnEntities() {
        List<User> users = IntStream.range(1, 11)
                .mapToObj(index -> createUser(String.format("Pavel%d", index)))
                .collect(Collectors.toList());
        SearchCriterion searchCriterion = new SearchCriterion();
        searchCriterion.setOperation(SearchOperation.STARTS_WITH);
        searchCriterion.setField(NAME_FIELD);
        searchCriterion.setValue("Pavel,STRING");
        PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(NAME_FIELD).descending());
        userDynamoDbService.saveAll(users);

        List<User> result = userDynamoDbService.findAll(Set.of(searchCriterion), pageRequest);

        assertThat(result)
                .extracting(Collection::stream)
                .extracting(userStream -> userStream.map(User::getName))
                .extracting(stringStream -> stringStream.collect(Collectors.toList()))
                .asList()
                .contains("Pavel6", "Pavel5", "Pavel4");
    }

    @Test
    void findAll_Contains_ShouldReturnEntities() {
        List<User> users = IntStream.range(1, 11)
                .mapToObj(index -> createUser(String.format("Pavel%d", index)))
                .collect(Collectors.toList());
        SearchCriterion searchCriterion = new SearchCriterion();
        searchCriterion.setOperation(SearchOperation.CONTAINS);
        searchCriterion.setField(NAME_FIELD);
        searchCriterion.setValue("1,STRING");
        userDynamoDbService.saveAll(users);

        List<User> result = userDynamoDbService.findAll(Set.of(searchCriterion), null);

        assertThat(result)
                .extracting(Collection::stream)
                .extracting(userStream -> userStream.map(User::getName))
                .extracting(stringStream -> stringStream.collect(Collectors.toList()))
                .asList()
                .contains("Pavel1", "Pavel10");
    }

    private void createDefaultTableIfNotExists() {
        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(CAPACITY, CAPACITY);
        List<KeySchemaElement> keySchemaElements = new ArrayList<>();
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        keySchemaElements.add(new KeySchemaElement(ID_FIELD, KeyType.HASH));
        attributeDefinitions.add(new AttributeDefinition(ID_FIELD, ScalarAttributeType.S));
        createTableIfNotExists(keySchemaElements, attributeDefinitions, provisionedThroughput);
    }

    private void createTableIfNotExists(List<KeySchemaElement> keySchemaElements,
                                        List<AttributeDefinition> attributeDefinitions,
                                        ProvisionedThroughput provisionedThroughput) {
        CreateTableRequest createTableRequest = new CreateTableRequest()
                .withTableName(userDynamoDbService.getEntityClass().getSimpleName())
                .withProvisionedThroughput(provisionedThroughput)
                .withKeySchema(keySchemaElements)
                .withAttributeDefinitions(attributeDefinitions);
        createTableAndWaitUntilActive(createTableRequest);
    }

    @SneakyThrows
    private void createTableAndWaitUntilActive(CreateTableRequest createTableRequest) {
        boolean isTableCreated = TableUtils.createTableIfNotExists(amazonDynamoDB, createTableRequest);
        if (isTableCreated) {
            TableUtils.waitUntilActive(amazonDynamoDB, createTableRequest.getTableName());
        }
    }

    private User createUser(String userName) {
        User user = new User();
        user.setName(userName);
        return user;
    }
}