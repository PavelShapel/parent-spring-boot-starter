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
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class GenericCollectionRandomizer implements CollectionRandomizer {
    @Autowired
    RandomizerFactory genericRandomizerFactory;

    @Override
    public Map<String, Object> randomize(Entity map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        this::randomize)
                );
    }

    private Object randomize(Map.Entry<String, Specification> entry) {
        Specification specification = entry.getValue();
        return Optional.of(specification)
                .filter(not(this::isEntity))
                .map(this::randomize)
                .orElseGet(() -> randomize(getSpecificationBody(specification)));
    }

    private Entity getSpecificationBody(Specification specification) {
        return Optional.ofNullable(specification)
                .map(Specification::getBody)
                .orElseThrow(() -> new IllegalArgumentException("set correct entity field with body"));
    }

    private boolean isEntity(Specification specification) {
        String className = Entity.class.getSimpleName();
        return className.equalsIgnoreCase(specification.getType());
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
