package com.pavelshapel.jpa.spring.boot.starter.repository.search;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import lombok.Data;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Data
public abstract class SearchSpecification<T extends AbstractEntity> implements Specification<T> {
    @Autowired
    private ObjectFactory<SearchCriteria> searchCriteriaFactory;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        SearchCriteria searchCriteria = searchCriteriaFactory.getObject();
        switch (searchCriteria.getOperation()) {
            case EQUAL:
                return builder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            case NOT_EQUAL:
                return builder.notEqual(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
            case GREATER_THAN_OR_EQUAL_TO:
                return builder.greaterThanOrEqualTo(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
            case LESS_THAN_OR_EQUAL_TO:
                return builder.lessThanOrEqualTo(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
            case LIKE:
                return builder.like(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
            case STARTS_WITH:
                return builder.like(root.get(searchCriteria.getKey()), searchCriteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue());
            case CONTAINS:
                return builder.like(root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%");
            case IS_NULL:
                return builder.isNull(root.<String>get(searchCriteria.getKey()));
            case IS_NOT_NULL:
                return builder.isNotNull(root.<String>get(searchCriteria.getKey()));
            default:
                throw new IllegalArgumentException(String.format("search criteria operation [%s] not implemented", searchCriteria.getOperation()));
        }
    }
}