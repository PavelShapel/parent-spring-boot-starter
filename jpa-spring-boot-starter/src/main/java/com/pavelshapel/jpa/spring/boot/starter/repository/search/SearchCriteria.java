package com.pavelshapel.jpa.spring.boot.starter.repository.search;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchCriteria {
    private String key;
    private Object value;
    private SearchOperation operation;
}