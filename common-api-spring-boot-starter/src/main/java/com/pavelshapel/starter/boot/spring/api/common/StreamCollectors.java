package com.pavelshapel.starter.boot.spring.api.common;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.stream.Collector;

public interface StreamCollectors {
  static <T> Collector<T, ?, T> toSingle() {
    return collectingAndThen(
        toList(),
        list -> {
          if (list.size() != 1) {
            throw new IllegalArgumentException("Expected exactly 1 element, got " + list.size());
          }
          return list.getFirst();
        });
  }
}
