package com.pavelshapel.random.spring.boot.starter.randomizer.service.collection.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.entity.Entity;
import com.pavelshapel.random.spring.boot.starter.randomizer.entity.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.collection.CollectionRandomizer;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.factory.impl.GenericRandomizerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class GenericCollectionRandomizer implements CollectionRandomizer {
    @Autowired
    private GenericRandomizerFactory genericRandomizerFactory;

    @Override
    public Map<String, Object> randomize(Entity map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> randomize(entry.getValue()))
                );
    }

    @Override
    public Collection<Object> randomize(Collection<Specification> collection) {
        return collection.stream()
                .map(this::randomize)
                .collect(Collectors.toList());
    }

    private Object randomize(Specification specification) {
        return genericRandomizerFactory.getRandomizer(specification).randomize(specification);
    }
}
