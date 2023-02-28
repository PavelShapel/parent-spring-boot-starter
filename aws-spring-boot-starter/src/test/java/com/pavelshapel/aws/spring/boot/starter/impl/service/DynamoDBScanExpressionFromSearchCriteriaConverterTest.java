package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.pavelshapel.aws.spring.boot.starter.api.service.ScanExpressionFromSearchCriteriaConverter;
import com.pavelshapel.core.spring.boot.starter.api.model.Named;
import com.pavelshapel.core.spring.boot.starter.enums.PrimitiveType;
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

import static com.pavelshapel.jpa.spring.boot.starter.service.search.SearchOperation.IS_NOT_NULL;
import static com.pavelshapel.jpa.spring.boot.starter.service.search.SearchOperation.IS_NULL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = DynamoDBScanExpressionFromSearchCriteriaConverter.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class DynamoDBScanExpressionFromSearchCriteriaConverterTest {
    @Autowired
    ScanExpressionFromSearchCriteriaConverter scanExpressionFromSearchCriteriaConverter;

    @ParameterizedTest
    @EnumSource(SearchOperation.class)
    void convert_WithValidParameter_ShouldReturnAppropriateDynamoDBQueryExpression(SearchOperation searchOperation) {
        SearchCriterion searchCriterion = SearchCriterion.builder()
                .operation(searchOperation)
                .field(Named.NAME_FIELD)
                .value(String.format("test<%s>", PrimitiveType.STRING.name()))
                .build();

        DynamoDBScanExpression result = scanExpressionFromSearchCriteriaConverter.convert(Set.of(searchCriterion));

        assertThat(result)
                .hasFieldOrPropertyWithValue("filterExpression", String.format(searchOperation.getSearchOperationPattern(), Named.NAME_FIELD))
                .hasFieldOrPropertyWithValue("expressionAttributeNames", Map.of(String.format("#%s", Named.NAME_FIELD), Named.NAME_FIELD))
                .hasFieldOrPropertyWithValue("expressionAttributeValues",
                        IS_NULL.equals(searchOperation) || IS_NOT_NULL.equals(searchOperation) ? null : Map.of(String.format(":%s", Named.NAME_FIELD), new AttributeValue().withS("test")));

    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void convert_WithEmptyParameter_ShouldReturnAppropriateDynamoDBQueryExpression(Set<SearchCriterion> searchCriteria) {
        DynamoDBScanExpression result = scanExpressionFromSearchCriteriaConverter.convert(searchCriteria);

        assertThat(result)
                .hasFieldOrPropertyWithValue("filterExpression", null)
                .hasFieldOrPropertyWithValue("expressionAttributeNames", null)
                .hasFieldOrPropertyWithValue("expressionAttributeValues", null);

    }
}