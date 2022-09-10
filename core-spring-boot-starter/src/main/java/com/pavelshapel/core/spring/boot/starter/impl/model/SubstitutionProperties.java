package com.pavelshapel.core.spring.boot.starter.impl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SubstitutionProperties extends HashMap<String, String> {
    public SubstitutionProperties() {
    }

    public SubstitutionProperties(Map<String, String> map) {
        super(map);
    }
}