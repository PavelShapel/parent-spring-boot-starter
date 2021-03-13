package com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.entity.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.AbstractRandomizer;

import java.util.concurrent.ThreadLocalRandom;

public class LongRandomizer extends AbstractRandomizer<Long> {
    @Override
    public Long randomize(Specification specification) {
        return ThreadLocalRandom.current().nextLong(
                specification.getMin(),
                specification.getMax()
        );
    }
}
