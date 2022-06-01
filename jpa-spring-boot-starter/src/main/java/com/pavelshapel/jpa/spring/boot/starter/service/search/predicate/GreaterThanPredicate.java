package com.pavelshapel.jpa.spring.boot.starter.service.search.predicate;

public class GreaterThanPredicate extends AbstractPredicate {
    public GreaterThanPredicate(Object pattern) {
        super(pattern);
    }

    @Override
    public boolean test(Comparable<Object> comparable) {
        return comparable.compareTo(getPattern()) > 0;
    }
}
