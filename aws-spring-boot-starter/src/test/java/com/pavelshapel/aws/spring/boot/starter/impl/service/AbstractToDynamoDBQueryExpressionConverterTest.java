package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.pavelshapel.aws.spring.boot.starter.api.service.ToDynamoDBQueryExpressionConverter;
import com.pavelshapel.aws.spring.boot.starter.context.User;
import com.pavelshapel.aws.spring.boot.starter.context.UserToDynamoDBQueryExpressionConverter;
import com.pavelshapel.core.spring.boot.starter.api.model.Named;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchOperation;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = UserToDynamoDBQueryExpressionConverter.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class AbstractToDynamoDBQueryExpressionConverterTest {
    @Autowired
    ToDynamoDBQueryExpressionConverter<User> userToDynamoDBQueryExpressionConverter;

    @ParameterizedTest
    @EnumSource(SearchOperation.class)
    void convert_WithValidParameter_ShouldReturnAppropriateDynamoDBQueryExpression(SearchOperation searchOperation) {
        SearchCriterion searchCriterion = new SearchCriterion();
        searchCriterion.setOperation(searchOperation);
        searchCriterion.setField(Named.NAME_FIELD);
        searchCriterion.setValue("Pavel,STRING");

        DynamoDBQueryExpression<User> result = userToDynamoDBQueryExpressionConverter.convert(Set.of(searchCriterion));

        assertThat(result)
                .hasFieldOrPropertyWithValue("keyConditionExpression", String.format(searchOperation.getSearchOperationPattern(), Named.NAME_FIELD))
                .hasFieldOrPropertyWithValue("expressionAttributeValues", Map.of(String.format(":%s", Named.NAME_FIELD), new AttributeValue().withS("Pavel")));

    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void convert_WithEmptyParameter_ShouldReturnAppropriateDynamoDBQueryExpression(Set<SearchCriterion> searchCriteria) {
        DynamoDBQueryExpression<User> result = userToDynamoDBQueryExpressionConverter.convert(searchCriteria);

        assertThat(result)
                .hasFieldOrPropertyWithValue("keyConditionExpression", null)
                .hasFieldOrPropertyWithValue("expressionAttributeValues", null);

    }
}