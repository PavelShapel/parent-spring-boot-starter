package com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded;

import com.pavelshapel.core.spring.boot.starter.bean.AbstractBeansCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class BoundedTypeBeansCollection extends AbstractBeansCollection<BoundedType<?>> {
    @Autowired
    private Map<String, BoundedType<?>> beans;

    @Override
    public Map<String, BoundedType<?>> getBeans() {
        return beans;
    }
}
