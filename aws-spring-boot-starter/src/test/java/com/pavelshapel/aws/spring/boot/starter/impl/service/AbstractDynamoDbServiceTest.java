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
import com.pavelshapel.aws.spring.boot.starter.provider.OneByteProvider;
import com.pavelshapel.aws.spring.boot.starter.provider.OneStringProvider;
import com.pavelshapel.aws.spring.boot.starter.provider.TwoStringProvider;
import com.pavelshapel.core.spring.boot.starter.enums.PrimitiveType;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchOperation;
import com.pavelshapel.test.spring.boot.starter.container.DynamoDbAwsExtension;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.stream.IntStream;

import static com.pavelshapel.core.spring.boot.starter.api.model.Entity.ID_FIELD;
import static com.pavelshapel.core.spring.boot.starter.api.model.Named.NAME_FIELD;
import static com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity.PARENT_FIELD;
import static com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity.ROOT_ID;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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
        userDynamoDbService.deleteAll(userDynamoDbService.findAll());
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void save_RootWithValidParameter_ShouldSaveAndReturnEntity(String userName) {
        User rootUser = createUser(null, userName);

        User result = userDynamoDbService.save(rootUser);
        int count = userDynamoDbService.getCount();
        User rootConstraintEntity = userDynamoDbService.findById(ROOT_ID);

        assertThat(result)
                .hasFieldOrPropertyWithValue(NAME_FIELD, rootUser.getName())
                .hasFieldOrPropertyWithValue(PARENT_FIELD, rootUser.getParent())
                .extracting(User::getId)
                .isNotNull();
        assertThat(count)
                .isEqualTo(2);
        assertThat(rootConstraintEntity)
                .hasFieldOrPropertyWithValue(ID_FIELD, ROOT_ID);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void save_RootTwiceWithValidParameter_ShouldThrowException(String userName) {
        User rootUser = createUser(null, userName);
        userDynamoDbService.save(rootUser);
        User newRootUser = createUser(null, userName);

        assertThatThrownBy(() -> userDynamoDbService.save(newRootUser))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void save_ChildWithValidParameter_ShouldSaveAndReturnEntity(String userName) {
        User rootUser = createUser(null, userName);
        userDynamoDbService.save(rootUser);
        User childUser = createUser(rootUser, userName);

        User result = userDynamoDbService.save(childUser);

        assertThat(result)
                .hasFieldOrPropertyWithValue(NAME_FIELD, childUser.getName())
                .hasFieldOrPropertyWithValue(PARENT_FIELD, childUser.getParent())
                .extracting(User::getId)
                .isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(TwoStringProvider.class)
    void save_ChildWithoutParent_ShouldThrowException(String id, String userName) {
        User rootUser = createUser(null, userName);
        rootUser.setId(id);
        User childUser = createUser(rootUser, userName);

        assertThatThrownBy(() -> userDynamoDbService.save(childUser))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(TwoStringProvider.class)
    void save_UpdateChildWithoutParent_ShouldThrowException(String id, String userName) {
        User rootUser = createUser(null, userName);
        userDynamoDbService.save(rootUser);
        User childUser = createUser(rootUser, userName);
        userDynamoDbService.save(childUser);
        User newRootUser = createUser(null, userName);
        newRootUser.setId(id);

        childUser.setParent(newRootUser);

        assertThatThrownBy(() -> userDynamoDbService.save(childUser))
                .isInstanceOf(IllegalArgumentException.class);
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
        User user = createUser(null, userName);

        List<User> result = userDynamoDbService.saveAll(List.of(user));

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
    void saveAll_WithNullOrEmptyParameter_ShouldThrowException(List<User> users) {
        assertThatThrownBy(() -> userDynamoDbService.saveAll(users))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void findById_WithValidParameter_ShouldReturnEntity(String userName) {
        User user = createUser(null, userName);
        userDynamoDbService.save(user);

        User result = userDynamoDbService.findById(user.getId());

        assertThat(result).isEqualTo(user);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void findById_WithNullParameter_ShouldThrowException(String id) {
        assertThatThrownBy(() -> userDynamoDbService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void findAll_StartsWith_ShouldReturnEntities(String userName) {
        User rootUser = createUser(null, userName);
        userDynamoDbService.save(rootUser);
        List<User> users = IntStream.range(1, 11)
                .mapToObj(index -> createUser(rootUser, String.format("%s_%d", userName, index)))
                .toList();
        userDynamoDbService.saveAll(users);
        SearchCriterion searchCriterion = SearchCriterion.builder()
                .operation(SearchOperation.STARTS_WITH)
                .field(NAME_FIELD)
                .value(String.format("%s_<%s>", userName, PrimitiveType.STRING.name()))
                .build();
        PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(NAME_FIELD).descending());

        List<User> result = userDynamoDbService.findAll(Set.of(searchCriterion), pageRequest);

        assertThat(result)
                .extracting(Collection::stream)
                .extracting(userStream -> userStream.map(User::getName))
                .extracting(stringStream -> stringStream.toList())
                .asList()
                .contains(String.format("%s_6", userName))
                .contains(String.format("%s_5", userName))
                .contains(String.format("%s_4", userName));
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void findAll_Contains_ShouldReturnEntities(String userName) {
        User rootUser = createUser(null, userName);
        userDynamoDbService.save(rootUser);
        List<User> users = IntStream.range(1, 11)
                .mapToObj(index -> createUser(rootUser, String.format("%s_%d", userName, index)))
                .toList();
        userDynamoDbService.saveAll(users);
        SearchCriterion searchCriterion = SearchCriterion.builder()
                .operation(SearchOperation.CONTAINS)
                .field(NAME_FIELD)
                .value(String.format("_1<%s>", PrimitiveType.STRING.name()))
                .build();
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(NAME_FIELD).descending());

        List<User> result = userDynamoDbService.findAll(Set.of(searchCriterion), pageRequest);

        assertThat(result)
                .extracting(Collection::stream)
                .extracting(userStream -> userStream.map(User::getName))
                .extracting(stringStream -> stringStream.toList())
                .asList()
                .contains(String.format("%s_1", userName))
                .contains(String.format("%s_10", userName));
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void deleteById_WithValidParameter_ShouldDeleteEntity(String userName) {
        User user = createUser(null, userName);
        userDynamoDbService.save(user);

        String id = user.getId();
        userDynamoDbService.deleteById(id);
        User result = userDynamoDbService.findById(id);

        assertThat(result).isNull();
    }

    @ParameterizedTest
    @NullSource
    void deleteById_WithNullParameter_ShouldThrowException(String id) {
        assertThatThrownBy(() -> userDynamoDbService.deleteById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(OneByteProvider.class)
    void getCount_WithValidParameter_ShouldReturnCount(Byte count) {
        User rootUser = createUser(null, "root");
        userDynamoDbService.save(rootUser);
        int verifiedCount = min(abs(count), 20);
        List<User> users = IntStream.range(0, verifiedCount)
                .mapToObj(index -> createUser(rootUser, String.format("user_%d", index)))
                .toList();
        userDynamoDbService.saveAll(users);

        int result = userDynamoDbService.getCount();

        assertThat(result)
                .isEqualTo(verifiedCount + 2);
    }

    @ParameterizedTest
    @ArgumentsSource(OneByteProvider.class)
    void getChildren_WithValidParameter_ShouldReturnChildren(Byte count) {
        User rootUser = createUser(null, "root");
        userDynamoDbService.save(rootUser);
        int verifiedCount = min(abs(count), 20);
        List<User> users = IntStream.range(0, verifiedCount)
                .mapToObj(index -> createUser(rootUser, String.format("user_%d", index)))
                .toList();
        userDynamoDbService.saveAll(users);

        List<User> result = userDynamoDbService.getChildren(rootUser.getId());

        assertThat(result)
                .asList()
                .hasSameElementsAs(users);
    }

    @ParameterizedTest
    @NullSource
    void getChildren_WithNullParameter_ShouldThrowException(String id) {
        assertThatThrownBy(() -> userDynamoDbService.getChildren(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void getParentage_WithValidParameter_ShouldReturnParentage(String userName) {
        User rootUser = createUser(null, "root");
        userDynamoDbService.save(rootUser);
        User childUser = createUser(rootUser, "child");
        userDynamoDbService.save(childUser);
        User leafUser = createUser(childUser, "leaf");
        userDynamoDbService.save(leafUser);

        List<User> result = userDynamoDbService.getParentage(leafUser.getId());

        assertThat(result)
                .isEqualTo(List.of(leafUser, childUser, rootUser));
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

    private User createUser(User parent, String name) {
        User user = userDynamoDbService.create();
        user.setParent(parent);
        user.setName(name);
        return user;
    }
}