package com.pavelshapel.random.spring.boot.starter.randomizer.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Entity extends TreeMap<String, Specification> {
    public Entity() {
        super(Comparator.naturalOrder());
    }

    public Entity(Map<String, Specification> map) {
        super(map);
    }
}
