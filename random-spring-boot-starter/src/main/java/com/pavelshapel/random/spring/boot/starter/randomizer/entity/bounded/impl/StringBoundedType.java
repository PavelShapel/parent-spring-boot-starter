package com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.BoundedType;
import org.apache.commons.lang3.Range;

import static com.pavelshapel.random.spring.boot.starter.randomizer.enums.DefaultRanges.DEFAULT_NATURAL_RANGE;

public class StringBoundedType implements BoundedType<String> {
    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public Range<Long> getRange() {
        return DEFAULT_NATURAL_RANGE.getValue();
    }
}
