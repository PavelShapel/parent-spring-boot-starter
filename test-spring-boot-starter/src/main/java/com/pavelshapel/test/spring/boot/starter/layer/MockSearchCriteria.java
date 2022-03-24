package com.pavelshapel.test.spring.boot.starter.layer;

import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchCriteria;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchOperation;

public interface MockSearchCriteria {
    default SearchCriteria getMockSearchCriteria(String value, String field, SearchOperation searchOperation) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setField(field);
        searchCriteria.setValue(value);
        searchCriteria.setOperation(searchOperation);
        return searchCriteria;
    }

    default SearchCriteria getMockSearchCriteriaId(String value, SearchOperation searchOperation) {
        return getMockSearchCriteria(value, "id", searchOperation);
    }

    default SearchCriteria getMockSearchCriteriaParent(String value, SearchOperation searchOperation) {
        return getMockSearchCriteria(value, "parent", searchOperation);
    }

    default SearchCriteria getMockSearchCriteriaName(String value, SearchOperation searchOperation) {
        return getMockSearchCriteria(value, "name", searchOperation);
    }
}