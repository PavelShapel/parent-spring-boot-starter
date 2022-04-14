package com.pavelshapel.random.spring.boot.starter.randomizer.service.factory.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.RandomizerBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.factory.RandomizerFactory;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.Randomizer;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Predicate;

public class GenericRandomizerFactory implements RandomizerFactory {
    @Autowired
    private RandomizerBeansCollection randomizerBeansCollection;

    @Override
    public Randomizer<?> getRandomizer(Specification specification) {
        return getRandomizer(specification.getType());
    }

    @Override
    public Randomizer<?> getRandomizer(String type) {
        return randomizerBeansCollection.getBean(isSpecificationTypeEqualsType(type))
                .orElseThrow(() -> new NotImplementedException(String.format("randomizer not implemented for [%s] type", type)));
    }

    private Predicate<Randomizer<?>> isSpecificationTypeEqualsType(String type) {
        return randomizer -> randomizer.createDefaultSpecification().getType().equalsIgnoreCase(type);
    }
}
