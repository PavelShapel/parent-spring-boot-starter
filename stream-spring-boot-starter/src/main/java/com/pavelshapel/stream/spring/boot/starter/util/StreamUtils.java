package com.pavelshapel.stream.spring.boot.starter.util;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StreamUtils {
    public <T> Collector<T, ?, Optional<T>> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        return Optional.empty();
                    }
                    return Optional.of(list.get(0));
                }
        );
    }

    public <T> Collector<T, ?, ResponseEntity<List<T>>> toResponseEntityList() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                ResponseEntity::ok
        );
    }
}