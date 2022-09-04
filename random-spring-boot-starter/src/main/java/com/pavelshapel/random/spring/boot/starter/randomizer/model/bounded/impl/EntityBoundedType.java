package com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.Entity;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.AbstractBoundedType;
import org.apache.commons.lang3.Range;

import static com.pavelshapel.random.spring.boot.starter.randomizer.enums.DefaultRanges.DEFAULT_LONG_RANGE;

public class EntityBoundedType extends AbstractBoundedType<Entity> {
    @Override
    public Range<Long> getRange() {
        return DEFAULT_LONG_RANGE.getValue();
    }
}
