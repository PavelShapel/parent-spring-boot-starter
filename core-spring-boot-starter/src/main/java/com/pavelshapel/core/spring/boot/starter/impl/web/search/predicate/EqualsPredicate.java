package com.pavelshapel.core.spring.boot.starter.impl.web.search.predicate;

public class EqualsPredicate extends AbstractPredicate {
    public EqualsPredicate(Object pattern) {
        super(pattern);
    }

    @Override
    public boolean test(Comparable<Object> comparable) {
        return comparable.compareTo(getPattern()) == 0;
    }
}
