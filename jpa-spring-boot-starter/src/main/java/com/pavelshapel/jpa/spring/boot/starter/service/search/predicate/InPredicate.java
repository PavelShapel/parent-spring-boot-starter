package com.pavelshapel.jpa.spring.boot.starter.service.search.predicate;

import java.util.Arrays;

public class InPredicate extends AbstractPredicate {
    public InPredicate(Object pattern) {
        super(pattern);
    }

    @Override
    public boolean test(Comparable<Object> comparable) {
        return Arrays.asList(getPattern().toString().split(",")).contains(comparable.toString());
    }
}
