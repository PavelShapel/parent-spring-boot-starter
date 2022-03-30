package com.pavelshapel.test.spring.boot.starter.layer;


import com.pavelshapel.core.spring.boot.starter.enums.PrimitiveType;
import com.pavelshapel.core.spring.boot.starter.impl.web.search.SearchCriteria;
import com.pavelshapel.core.spring.boot.starter.impl.web.search.SearchOperation;

public interface MockSearchCriteria {
    default SearchCriteria getMockSearchCriteria(String value, PrimitiveType valueType, String field, SearchOperation searchOperation) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setField(field);
        searchCriteria.setValue(String.format("%s,%s", value, valueType));
        searchCriteria.setOperation(searchOperation);
        return searchCriteria;
    }

    default SearchCriteria getMockSearchCriteriaId(String value, PrimitiveType valueType, SearchOperation searchOperation) {
        return getMockSearchCriteria(value, valueType, "id", searchOperation);
    }

    default SearchCriteria getMockSearchCriteriaParent(String value, PrimitiveType valueType, SearchOperation searchOperation) {
        return getMockSearchCriteria(value, valueType, "parent", searchOperation);
    }

    default SearchCriteria getMockSearchCriteriaName(String value, SearchOperation searchOperation) {
        return getMockSearchCriteria(value, PrimitiveType.STRING, "name", searchOperation);
    }
}