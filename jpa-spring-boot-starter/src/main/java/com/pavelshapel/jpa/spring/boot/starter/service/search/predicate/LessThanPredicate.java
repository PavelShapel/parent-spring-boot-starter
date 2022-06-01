package com.pavelshapel.jpa.spring.boot.starter.service.search.predicate;

public class LessThanPredicate extends AbstractPredicate {
    public LessThanPredicate(Object pattern) {
        super(pattern);
    }

    @Override
    public boolean test(Comparable<Object> comparable) {
        return comparable.compareTo(getPattern()) < 0;
    }
}
