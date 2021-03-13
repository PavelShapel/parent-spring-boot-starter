package com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded;

import com.pavelshapel.random.spring.boot.starter.randomizer.AbstractBeansCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public final class BoundedTypeBeansCollection extends AbstractBeansCollection<BoundedType<?>> {
    @Autowired
    private Map<String, BoundedType<?>> beans;

    @Override
    public Map<String, BoundedType<?>> getBeans() {
        return beans;
    }
}
