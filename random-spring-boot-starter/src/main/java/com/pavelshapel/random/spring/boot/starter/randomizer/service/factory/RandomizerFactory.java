package com.pavelshapel.random.spring.boot.starter.randomizer.service.factory;

import com.pavelshapel.random.spring.boot.starter.randomizer.entity.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.Randomizer;

public interface RandomizerFactory {
    Randomizer<?> getRandomizer(Specification specification);

    Randomizer<?> getRandomizer(String type);
}
