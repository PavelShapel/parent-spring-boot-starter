package com.pavelshapel.jpa.spring.boot.starter.repository.search;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SearchCriteria {
    @NotBlank
    private String field;
    private Object value;
    private SearchOperation operation = SearchOperation.EQUAL;
}