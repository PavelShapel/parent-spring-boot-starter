package com.pavelshapel.core.spring.boot.starter.impl.web.search.predicate;

public class LessThanPredicate extends AbstractPredicate {
    public LessThanPredicate(Object pattern) {
        super(pattern);
    }

    @Override
    public boolean test(Comparable<Object> comparable) {
        return comparable.compareTo(getPattern()) < 0;
    }
}
