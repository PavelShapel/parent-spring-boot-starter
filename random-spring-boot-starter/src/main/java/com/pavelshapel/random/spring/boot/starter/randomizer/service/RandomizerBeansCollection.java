package com.pavelshapel.random.spring.boot.starter.randomizer.service;

import com.pavelshapel.core.spring.boot.starter.bean.AbstractBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.Randomizer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public final class RandomizerBeansCollection extends AbstractBeansCollection<Randomizer<?>> {
    @Autowired
    private Map<String, Randomizer<?>> beans;

    @Override
    public Map<String, Randomizer<?>> getBeans() {
        return beans;
    }
}
