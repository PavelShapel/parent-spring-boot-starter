package com.pavelshapel.random.spring.boot.starter.randomizer.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Specification {
    String type;
    long min;
    long max;
}
