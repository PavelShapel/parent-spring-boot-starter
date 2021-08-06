package com.pavelshapel.stream.spring.boot.starter.util;

import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
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

    public <T> Collector<T, ?, List<T>> toList(boolean reverse) {
        return Collectors.collectingAndThen(
                Collectors.toList(), list -> {
                    if (reverse) {
                        Collections.reverse(list);
                    }
                    return list;
                });
    }

    public <T, K, U> Collector<T, ?, Map<K, U>> toMapOfNullables(Function<? super T, ? extends K> keyMapper,
                                                                 Function<? super T, ? extends U> valueMapper,
                                                                 BinaryOperator<U> mergeFunction) {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    Map<K, U> result = new HashMap<>();
                    for (T item : list) {
                        K key = keyMapper.apply(item);
                        U newValue = valueMapper.apply(item);
                        U value = result.containsKey(key) ? mergeFunction.apply(result.get(key), newValue) : newValue;
                        result.put(key, value);
                    }
                    return result;
                });
    }
}