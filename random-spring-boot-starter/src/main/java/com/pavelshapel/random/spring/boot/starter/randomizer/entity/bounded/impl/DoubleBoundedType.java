package com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.BoundedType;
import org.apache.commons.lang3.Range;

import static com.pavelshapel.random.spring.boot.starter.randomizer.enums.DefaultRanges.DEFAULT_LONG_RANGE;

public final class DoubleBoundedType implements BoundedType<Double> {
    @Override
    public Class<Double> getType() {
        return Double.class;
    }

    @Override
    public Range<Long> getRange() {
        return DEFAULT_LONG_RANGE.getValue();
    }
}
