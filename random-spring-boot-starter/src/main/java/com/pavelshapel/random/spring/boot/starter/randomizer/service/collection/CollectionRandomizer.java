package com.pavelshapel.random.spring.boot.starter.randomizer.service.collection;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.Entity;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.Specification;

import java.util.Collection;
import java.util.Map;

public interface CollectionRandomizer {
    Map<String, Object> randomize(Entity map);

    Collection<Object> randomize(Collection<Specification> collection);
}
