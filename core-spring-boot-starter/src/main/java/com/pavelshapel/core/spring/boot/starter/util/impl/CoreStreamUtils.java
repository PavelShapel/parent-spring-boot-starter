package com.pavelshapel.core.spring.boot.starter.util.impl;

import com.pavelshapel.core.spring.boot.starter.util.StreamUtils;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.Collections.*;

public class CoreStreamUtils implements StreamUtils {
    @Override
    public <T> Collector<T, ?, Optional<T>> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> list.size() == 1 ? Optional.of(list.get(0)) : Optional.empty()
        );
    }

    @Override
    public <T> Collector<T, ?, ResponseEntity<List<T>>> toResponseEntityList() {
        return toResponseEntityList(false);
    }

    @Override
    public <T> Collector<T, ?, ResponseEntity<List<T>>> toResponseEntityList(boolean reverse) {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (reverse) {
                        reverse(list);
                    }
                    return ResponseEntity.ok(list);
                });
    }

    @Override
    public <T> Collector<T, ?, List<T>> toList(boolean reverse) {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (reverse) {
                        reverse(list);
                    }
                    return list;
                });
    }

    @Override
    public <T, K, V> Collector<T, ?, Map<K, V>> toMapOfNullables(Function<? super T, ? extends K> keyMapper,
                                                                 Function<? super T, ? extends V> valueMapper,
                                                                 BinaryOperator<V> mergeFunction) {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    Map<K, V> result = new HashMap<>();
                    for (T item : list) {
                        K key = keyMapper.apply(item);
                        V newValue = valueMapper.apply(item);
                        V value = result.containsKey(key) ? mergeFunction.apply(result.get(key), newValue) : newValue;
                        result.put(key, value);
                    }
                    return result;
                });
    }
}