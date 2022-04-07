package com.pavelshapel.core.spring.boot.starter.impl.web.search.predicate;

import java.util.Objects;

public class IsNullPredicate extends AbstractPredicate {
    public IsNullPredicate(Object pattern) {
        super(pattern);
    }

    @Override
    public boolean test(Comparable<Object> comparable) {
        return Objects.isNull(comparable);
    }
}
