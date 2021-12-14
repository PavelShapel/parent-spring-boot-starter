package com.pavelshapel.jpa.spring.boot.starter.repository.search;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SearchCriteria {
    public static final String DEFAULT_FIELD = "id";
    public static final String DEFAULT_VALUE = "0";

    @NotBlank
    private String field = DEFAULT_FIELD;
    private Object value = DEFAULT_VALUE;
    private SearchOperation operation = SearchOperation.GREATER_THAN_OR_EQUAL_TO;
}