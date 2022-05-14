package com.pavelshapel.test.spring.boot.starter.layer;


import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.model.Named;
import com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity;
import com.pavelshapel.core.spring.boot.starter.enums.PrimitiveType;
import com.pavelshapel.core.spring.boot.starter.impl.web.search.SearchCriterion;
import com.pavelshapel.core.spring.boot.starter.impl.web.search.SearchOperation;

public interface MockSearchCriterion {
    default SearchCriterion getMockSearchCriterion(String value, PrimitiveType valueType, String field, SearchOperation searchOperation) {
        SearchCriterion searchCriterion = new SearchCriterion();
        searchCriterion.setField(field);
        searchCriterion.setValue(String.format("%s,%s", value, valueType));
        searchCriterion.setOperation(searchOperation);
        return searchCriterion;
    }

    default SearchCriterion getMockSearchCriterionId(String value, PrimitiveType valueType, SearchOperation searchOperation) {
        return getMockSearchCriterion(value, valueType, Entity.ID_FIELD, searchOperation);
    }

    default SearchCriterion getMockSearchCriterionParent(String value, PrimitiveType valueType, SearchOperation searchOperation) {
        return getMockSearchCriterion(value, valueType, ParentalEntity.PARENT_FIELD, searchOperation);
    }

    default SearchCriterion getMockSearchCriterionName(String value, SearchOperation searchOperation) {
        return getMockSearchCriterion(value, PrimitiveType.STRING, Named.NAME_FIELD, searchOperation);
    }
}