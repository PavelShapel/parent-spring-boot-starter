package com.pavelshapel.random.spring.boot.starter.randomizer.service.factory.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.entity.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.RandomizerBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.factory.RandomizerFactory;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.Randomizer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Predicate;

public final class GenericRandomizerFactory implements RandomizerFactory {
    @Autowired
    private RandomizerBeansCollection randomizerBeansCollection;

    @Override
    public Randomizer<?> getRandomizer(Specification specification) {
        return getRandomizer(specification.getType());
    }

    @Override
    public Randomizer<?> getRandomizer(String type) {
        return randomizerBeansCollection.getBean(getPredicate(type))
                .orElseThrow(() -> new IllegalArgumentException(type));
    }

    private Predicate<Randomizer<?>> getPredicate(String type) {
        return randomizer -> randomizer.createDefaultSpecification().getType().equalsIgnoreCase(type);
    }
}
