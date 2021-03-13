package com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.BoundedType;
import org.apache.commons.lang3.Range;

import java.util.Date;

import static com.pavelshapel.random.spring.boot.starter.randomizer.enums.DefaultRanges.DEFAULT_YEAR_RANGE;

public class DateBoundedType implements BoundedType<Date> {
    @Override
    public Class<Date> getType() {
        return Date.class;
    }

    @Override
    public Range<Long> getRange() {
        return DEFAULT_YEAR_RANGE.getValue();
    }
}
