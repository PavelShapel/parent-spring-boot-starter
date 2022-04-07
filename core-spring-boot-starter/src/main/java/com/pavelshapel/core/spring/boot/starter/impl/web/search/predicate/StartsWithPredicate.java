package com.pavelshapel.core.spring.boot.starter.impl.web.search.predicate;

public class StartsWithPredicate extends AbstractPredicate {
    public StartsWithPredicate(Object pattern) {
        super(pattern);
    }

    @Override
    public boolean test(Comparable<Object> comparable) {
        return comparable.toString().startsWith(getPattern().toString());
    }
}
