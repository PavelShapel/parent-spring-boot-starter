package com.pavelshapel.jpa.spring.boot.starter.repository.search;

import lombok.Data;

@Data
public class SearchCriteria {
    private String field;
    private Object value;
    private SearchOperation operation = SearchOperation.EQUAL;
}