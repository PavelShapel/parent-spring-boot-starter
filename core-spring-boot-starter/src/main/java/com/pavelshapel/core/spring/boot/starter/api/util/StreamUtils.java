package com.pavelshapel.core.spring.boot.starter.api.util;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface StreamUtils {
    <T> Collector<T, ?, Optional<List<T>>> toOptionalList();

    <T> Collector<T, ?, Optional<T>> toSingleton();

    <T> Collector<T, ?, ResponseEntity<List<T>>> toResponseEntityList();

    <T> Collector<T, ?, ResponseEntity<List<T>>> toResponseEntityList(boolean reverse);

    <T> Collector<T, ?, List<T>> toList(boolean reverse);

    <T, K, V> Collector<T, ?, Map<K, V>> toMapOfNullables(Function<? super T, ? extends K> keyMapper,
                                                          Function<? super T, ? extends V> valueMapper,
                                                          BinaryOperator<V> mergeFunction);

    <T> Stream<T> iterableToStream(Iterable<T> iterable);

    <T> List<T> iterableToList(Iterable<T> iterable);
}
