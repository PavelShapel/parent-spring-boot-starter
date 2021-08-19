package com.pavelshapel.jpa.spring.boot.starter.repository.search;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Data
public abstract class SearchSpecification<T extends AbstractEntity> implements Specification<T> {
    private transient SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        String field = searchCriteria.getField();
        Object value = searchCriteria.getValue();
        switch (searchCriteria.getOperation()) {
            case EQUAL:
                return builder.equal(root.get(field), value);
            case NOT_EQUAL:
                return builder.notEqual(root.get(field), value);
            case GREATER_THAN:
                return builder.greaterThan(root.get(field), value.toString());
            case GREATER_THAN_OR_EQUAL_TO:
                return builder.greaterThanOrEqualTo(root.get(field), value.toString());
            case LESS_THAN:
                return builder.lessThan(root.get(field), value.toString());
            case LESS_THAN_OR_EQUAL_TO:
                return builder.lessThanOrEqualTo(root.get(field), value.toString());
            case LIKE:
                return builder.like(builder.lower(root.get(field)), builder.lower(builder.literal(value.toString())));
            case STARTS_WITH:
                return builder.like(builder.lower(root.get(field)), builder.lower(builder.literal(value + "%")));
            case ENDS_WITH:
                return builder.like(builder.lower(root.get(field)), builder.lower(builder.literal("%" + value)));
            case CONTAINS:
                return builder.like(builder.lower(root.get(field)), builder.lower(builder.literal("%" + value + "%")));
            case IS_NULL:
                return builder.isNull(root.get(field));
            case IS_NOT_NULL:
                return builder.isNotNull(root.get(field));
            default:
                throw new UnsupportedOperationException(String.format("search criteria operation [%s] not implemented", searchCriteria.getOperation()));
        }
    }
}