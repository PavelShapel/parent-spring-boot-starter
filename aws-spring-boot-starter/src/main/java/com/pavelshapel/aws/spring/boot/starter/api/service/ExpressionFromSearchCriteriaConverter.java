package com.pavelshapel.aws.spring.boot.starter.api.service;

import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import org.springframework.core.convert.converter.Converter;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.pavelshapel.jpa.spring.boot.starter.service.search.SearchOperation.IS_NOT_NULL;
import static com.pavelshapel.jpa.spring.boot.starter.service.search.SearchOperation.IS_NULL;
import static java.util.function.Predicate.not;

public interface ExpressionFromSearchCriteriaConverter<T> extends Converter<Set<SearchCriterion>, T> {
    default String updateExpression(Set<SearchCriterion> searchCriteria) {
        return searchCriteria.stream()
                .map(searchCriterion -> String.format(searchCriterion.getOperation().getSearchOperationPattern(), searchCriterion.getField()))
                .collect(Collectors.joining(" and "));
    }

    default Map<String, String> updateExpressionAttributeNames(Set<SearchCriterion> searchCriteria) {
        return searchCriteria.stream()
                .collect(Collectors.toMap(
                        searchCriterion -> String.format("#%s", searchCriterion.getField()),
                        SearchCriterion::getField));
    }

    default Map<String, AttributeValue> updateExpressionAttributeValues(Set<SearchCriterion> searchCriteria) {
        Map<String, AttributeValue> expressionAttributeValues = searchCriteria.stream()
                .filter(not(searchCriterion -> IS_NULL.equals(searchCriterion.getOperation())))
                .filter(not(searchCriterion -> IS_NOT_NULL.equals(searchCriterion.getOperation())))
                .collect(Collectors.toMap(searchCriterion -> String.format(":%s", searchCriterion.getField()),
                        searchCriterion -> ItemUtils.toAttributeValue(searchCriterion.getCastedValue())));
        return expressionAttributeValues.isEmpty() ? null : expressionAttributeValues;
    }
}
