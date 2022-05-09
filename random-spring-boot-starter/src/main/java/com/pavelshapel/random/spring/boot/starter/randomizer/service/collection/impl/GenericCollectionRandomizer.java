package com.pavelshapel.random.spring.boot.starter.randomizer.service.collection.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.Entity;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.collection.CollectionRandomizer;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.factory.RandomizerFactory;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenericCollectionRandomizer implements CollectionRandomizer {
    @Autowired
    RandomizerFactory genericRandomizerFactory;

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
