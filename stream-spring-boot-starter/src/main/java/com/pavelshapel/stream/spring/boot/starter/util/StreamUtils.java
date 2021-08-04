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
                list -> list.size() == 1 ? Optional.of(list.get(0)) : Optional.empty()
        );
    }

    public <T> Collector<T, ?, ResponseEntity<List<T>>> toResponseEntityList() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                ResponseEntity::ok
        );
    }
}