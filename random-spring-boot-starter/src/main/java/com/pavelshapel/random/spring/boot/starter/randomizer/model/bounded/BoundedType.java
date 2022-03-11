package com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded;

import org.apache.commons.lang3.Range;

public interface BoundedType<T> {
    Class<T> getType();

    Range<Long> getRange();
}
