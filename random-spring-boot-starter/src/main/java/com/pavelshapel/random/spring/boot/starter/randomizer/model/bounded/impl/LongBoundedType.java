package com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.BoundedType;
import org.apache.commons.lang3.Range;

import static com.pavelshapel.random.spring.boot.starter.randomizer.enums.DefaultRanges.DEFAULT_LONG_RANGE;

public class LongBoundedType implements BoundedType<Long> {
    @Override
    public Class<Long> getType() {
        return Long.class;
    }

    @Override
    public Range<Long> getRange() {
        return DEFAULT_LONG_RANGE.getValue();
    }
}
