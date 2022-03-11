package com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.Specification;

public interface Randomizer<T> {
    T randomize(Specification specification);

    T randomize();

    T randomize(long minValue, long maxValue);

    Specification createDefaultSpecification();
}
