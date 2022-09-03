package com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.AbstractRandomizer;

import java.util.concurrent.ThreadLocalRandom;

public final class LongRandomizer extends AbstractRandomizer<Long> {
    @Override
    public Long rawRandomize(Specification specification) {
        return ThreadLocalRandom.current().nextLong(
                specification.getMin(),
                specification.getMax()
        );
    }
}
