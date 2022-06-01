package com.pavelshapel.jpa.spring.boot.starter.service.search.predicate;

public class ContainsPredicate extends AbstractPredicate {
    public ContainsPredicate(Object pattern) {
        super(pattern);
    }

    @Override
    public boolean test(Comparable<Object> comparable) {
        return comparable.toString().toLowerCase().contains(getPattern().toString().toLowerCase());
    }
}
