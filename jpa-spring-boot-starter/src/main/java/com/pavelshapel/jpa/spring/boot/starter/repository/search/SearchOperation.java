package com.pavelshapel.jpa.spring.boot.starter.repository.search;

public enum SearchOperation {
    EQUALS,
    NOT_EQUALS,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL_TO,
    LESS_THAN,
    LESS_THAN_OR_EQUAL_TO,
    LIKE,
    STARTS_WITH,
    ENDS_WITH,
    CONTAINS,
    IS_NULL,
    IS_NOT_NULL
}