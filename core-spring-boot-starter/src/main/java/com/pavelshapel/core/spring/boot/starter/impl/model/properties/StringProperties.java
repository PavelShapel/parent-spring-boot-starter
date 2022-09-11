package com.pavelshapel.core.spring.boot.starter.impl.model.properties;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public final class StringProperties extends AbstractProperties<String> {
    public StringProperties(Map<String, String> map) {
        super(map);
    }
}