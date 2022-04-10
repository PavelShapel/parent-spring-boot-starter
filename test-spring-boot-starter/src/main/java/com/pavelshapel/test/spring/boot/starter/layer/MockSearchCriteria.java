package com.pavelshapel.test.spring.boot.starter.layer;


import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.model.Named;
import com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity;
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
        return getMockSearchCriteria(value, valueType, Entity.ID_FIELD, searchOperation);
    }

    default SearchCriteria getMockSearchCriteriaParent(String value, PrimitiveType valueType, SearchOperation searchOperation) {
        return getMockSearchCriteria(value, valueType, ParentalEntity.PARENT_FIELD, searchOperation);
    }

    default SearchCriteria getMockSearchCriteriaName(String value, SearchOperation searchOperation) {
        return getMockSearchCriteria(value, PrimitiveType.STRING, Named.NAME_FIELD, searchOperation);
    }
}