package com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded;

import org.apache.commons.lang3.Range;

public interface BoundedType<T> {
    Class<T> getType();

    Range<Long> getRange();
}
