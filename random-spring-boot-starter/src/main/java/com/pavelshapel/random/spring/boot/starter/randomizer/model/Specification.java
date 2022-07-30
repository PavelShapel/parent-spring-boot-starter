package com.pavelshapel.random.spring.boot.starter.randomizer.model;

import com.pavelshapel.core.spring.boot.starter.api.model.Typed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Specification implements Typed<String> {
    String type;
    long min;
    long max;
}
