package com.pavelshapel.core.spring.boot.starter.impl.model.properties;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractProperties<T> extends HashMap<String, T> {
    protected AbstractProperties(Map<String, T> map) {
        super(map);
    }
}