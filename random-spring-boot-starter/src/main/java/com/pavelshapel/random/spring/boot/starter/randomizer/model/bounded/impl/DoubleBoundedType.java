package com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.AbstractBoundedType;
import org.apache.commons.lang3.Range;

import static com.pavelshapel.random.spring.boot.starter.randomizer.enums.DefaultRanges.DEFAULT_LONG_RANGE;

public class DoubleBoundedType extends AbstractBoundedType<Double> {
    @Override
    public Range<Long> getRange() {
        return DEFAULT_LONG_RANGE.getValue();
    }
}
