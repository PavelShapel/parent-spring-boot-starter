package com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.AbstractRandomizer;

import java.util.concurrent.ThreadLocalRandom;

public final class BooleanRandomizer extends AbstractRandomizer<Boolean> {
    @Override
    public Boolean rawRandomize(Specification specification) {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
