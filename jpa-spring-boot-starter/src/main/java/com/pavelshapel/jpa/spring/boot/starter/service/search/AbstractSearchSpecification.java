package com.pavelshapel.jpa.spring.boot.starter.service.search;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.stream.StreamSupport;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
public abstract class AbstractSearchSpecification<T extends Entity<?>> implements Specification<T> {
    transient Iterable<SearchCriterion> searchCriteria;

    @Override
    public Predicate toPredicate(@NonNull Root<T> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder builder) {
        return builder.and(
                StreamSupport.stream(searchCriteria.spliterator(), false)
                        .map(searchCriterion -> getPredicate(searchCriterion, root, builder))
                        .toArray(Predicate[]::new));
    }

    private Predicate getPredicate(SearchCriterion searchCriterion, Root<T> root, CriteriaBuilder builder) {
        String field = searchCriterion.getField();
        Comparable<?> value = searchCriterion.getCastedValue();
        switch (searchCriterion.getOperation()) {
            case EQUALS:
                return builder.equal(root.get(field), value);
            case NOT_EQUALS:
                return builder.notEqual(root.get(field), value);
            case GREATER_THAN:
                return builder.greaterThan(root.get(field), value.toString());
            case GREATER_THAN_OR_EQUAL_TO:
                return builder.greaterThanOrEqualTo(root.get(field), value.toString());
            case LESS_THAN:
                return builder.lessThan(root.get(field), value.toString());
            case LESS_THAN_OR_EQUAL_TO:
                return builder.lessThanOrEqualTo(root.get(field), value.toString());
            case STARTS_WITH:
                return builder.like(builder.lower(root.get(field)), builder.lower(builder.literal(value + "%")));
            case CONTAINS:
                return builder.like(builder.lower(root.get(field)), builder.lower(builder.literal("%" + value + "%")));
            case IS_NULL:
                return builder.isNull(root.get(field));
            case IS_NOT_NULL:
                return builder.isNotNull(root.get(field));
            default:
                throw new UnsupportedOperationException(String.format("search criteria operation [%s] not implemented", searchCriterion.getOperation()));
        }
    }
}