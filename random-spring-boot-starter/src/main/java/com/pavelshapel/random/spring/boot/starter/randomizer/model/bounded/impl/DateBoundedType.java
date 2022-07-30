package com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.AbstractBoundedType;
import org.apache.commons.lang3.Range;

import java.util.Date;

import static com.pavelshapel.random.spring.boot.starter.randomizer.enums.DefaultRanges.DEFAULT_YEAR_RANGE;

public class DateBoundedType extends AbstractBoundedType<Date> {
    @Override
    public Range<Long> getRange() {
        return DEFAULT_YEAR_RANGE.getValue();
    }
}
