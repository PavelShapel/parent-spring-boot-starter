package com.pavelshapel.core.spring.boot.starter.impl.web.search.predicate;

public class ContainsPredicate extends AbstractPredicate {
    public ContainsPredicate(Object pattern) {
        super(pattern);
    }

    @Override
    public boolean test(Comparable<Object> comparable) {
        return comparable.toString().toLowerCase().contains(getPattern().toString().toLowerCase());
    }
}
