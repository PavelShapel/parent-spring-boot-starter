package com.pavelshapel.jpa.spring.boot.starter.service.search.predicate;

public class EndsWithPredicate extends AbstractPredicate {
    public EndsWithPredicate(Object pattern) {
        super(pattern);
    }

    @Override
    public boolean test(Comparable<Object> comparable) {
        return comparable.toString().toLowerCase().endsWith(getPattern().toString().toLowerCase());
    }
}
