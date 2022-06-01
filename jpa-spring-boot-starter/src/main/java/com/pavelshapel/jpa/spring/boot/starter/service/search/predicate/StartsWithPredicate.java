package com.pavelshapel.jpa.spring.boot.starter.service.search.predicate;

public class StartsWithPredicate extends AbstractPredicate {
    public StartsWithPredicate(Object pattern) {
        super(pattern);
    }

    @Override
    public boolean test(Comparable<Object> comparable) {
        return comparable.toString().toLowerCase().startsWith(getPattern().toString().toLowerCase());
    }
}
