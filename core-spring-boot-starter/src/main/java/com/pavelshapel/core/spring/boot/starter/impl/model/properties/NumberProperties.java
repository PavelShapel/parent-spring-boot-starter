package com.pavelshapel.core.spring.boot.starter.impl.model.properties;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public final class NumberProperties extends AbstractProperties<Number> {
    public NumberProperties(Map<String, Number> map) {
        super(map);
    }
}