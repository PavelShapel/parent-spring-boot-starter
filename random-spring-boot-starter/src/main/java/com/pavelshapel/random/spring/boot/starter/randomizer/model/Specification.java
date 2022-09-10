package com.pavelshapel.random.spring.boot.starter.randomizer.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderMethodName = "hiddenBuilder")
public class Specification {
    String type;
    long min;
    long max;
    Entity body;

    public static SpecificationBuilder builder(String type) {
        return hiddenBuilder().type(type);
    }
}
