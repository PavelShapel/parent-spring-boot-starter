package com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.AbstractRandomizer;

import java.util.concurrent.ThreadLocalRandom;

public class BooleanRandomizer extends AbstractRandomizer<Boolean> {
    @Override
    public Boolean randomize(Specification specification) {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
