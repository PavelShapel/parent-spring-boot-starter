package com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.AbstractRandomizer;
import org.apache.commons.lang3.RandomStringUtils;

public final class StringRandomizer extends AbstractRandomizer<String> {
    @Override
    public String rawRandomize(Specification specification) {
        return RandomStringUtils.randomAlphanumeric(
                Math.toIntExact(specification.getMin()),
                Math.toIntExact(specification.getMax())
        );
    }
}
