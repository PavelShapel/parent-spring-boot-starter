package com.pavelshapel.core.spring.boot.starter.impl.web.search.predicate;

public class EndsWithPredicate extends AbstractPredicate {
    public EndsWithPredicate(Object pattern) {
        super(pattern);
    }

    @Override
    public boolean test(Comparable<Object> comparable) {
        return comparable.toString().toLowerCase().endsWith(getPattern().toString().toLowerCase());
    }
}
