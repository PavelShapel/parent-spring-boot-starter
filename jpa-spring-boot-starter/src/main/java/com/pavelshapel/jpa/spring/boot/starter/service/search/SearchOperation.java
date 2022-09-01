package com.pavelshapel.jpa.spring.boot.starter.service.search;

import com.pavelshapel.jpa.spring.boot.starter.service.search.predicate.ContainsPredicate;
import com.pavelshapel.jpa.spring.boot.starter.service.search.predicate.EndsWithPredicate;
import com.pavelshapel.jpa.spring.boot.starter.service.search.predicate.EqualsPredicate;
import com.pavelshapel.jpa.spring.boot.starter.service.search.predicate.GreaterThanPredicate;
import com.pavelshapel.jpa.spring.boot.starter.service.search.predicate.IsNullPredicate;
import com.pavelshapel.jpa.spring.boot.starter.service.search.predicate.LessThanPredicate;
import com.pavelshapel.jpa.spring.boot.starter.service.search.predicate.StartsWithPredicate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.function.Function;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString
public enum SearchOperation {
    EQUALS(SearchOperation::equalsPredicate),
    NOT_EQUALS(SearchOperation::notEqualsPredicate),
    GREATER_THAN(SearchOperation::greaterThanPredicate),
    GREATER_THAN_OR_EQUAL_TO(SearchOperation::greaterThanOrEqualToPredicate),
    LESS_THAN(SearchOperation::lessThanPredicate),
    LESS_THAN_OR_EQUAL_TO(SearchOperation::lessThanOrEqualToPredicate),
    STARTS_WITH(SearchOperation::startsWithPredicate),
    ENDS_WITH(SearchOperation::endsWithPredicate),
    CONTAINS(SearchOperation::containsPredicate),
    IS_NULL(SearchOperation::isNullPredicate),
    IS_NOT_NULL(SearchOperation::isNotNullPredicate);

    Function<Comparable<?>, Predicate<Comparable<Object>>> function;

    private static Predicate<Comparable<Object>> equalsPredicate(Object pattern) {
        return new EqualsPredicate(pattern);
    }

    private static Predicate<Comparable<Object>> notEqualsPredicate(Object pattern) {
        return equalsPredicate(pattern).negate();
    }

    private static Predicate<Comparable<Object>> greaterThanPredicate(Object pattern) {
        return new GreaterThanPredicate(pattern);
    }

    private static Predicate<Comparable<Object>> lessThanOrEqualToPredicate(Object pattern) {
        return greaterThanPredicate(pattern).negate();
    }

    private static Predicate<Comparable<Object>> lessThanPredicate(Object pattern) {
        return new LessThanPredicate(pattern);
    }

    private static Predicate<Comparable<Object>> greaterThanOrEqualToPredicate(Object pattern) {
        return lessThanPredicate(pattern).negate();
    }

    private static Predicate<Comparable<Object>> isNullPredicate(Object pattern) {
        return new IsNullPredicate(pattern);
    }

    private static Predicate<Comparable<Object>> isNotNullPredicate(Object pattern) {
        return isNullPredicate(pattern).negate();
    }

    private static Predicate<Comparable<Object>> startsWithPredicate(Object pattern) {
        return new StartsWithPredicate(pattern);
    }

    private static Predicate<Comparable<Object>> endsWithPredicate(Object pattern) {
        return new EndsWithPredicate(pattern);
    }

    private static Predicate<Comparable<Object>> containsPredicate(Object pattern) {
        return new ContainsPredicate(pattern);
    }
}