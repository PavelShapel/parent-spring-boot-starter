package com.pavelshapel.random.spring.boot.starter.randomizer.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Specification {
    String type;
    long min;
    long max;
}
