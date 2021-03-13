package com.pavelshapel.random.spring.boot.starter.randomizer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class Specification {
    private String type;
    private long min;
    private long max;
}
