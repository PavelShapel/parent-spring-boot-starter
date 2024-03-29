package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.api.util.StreamUtils;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Collections.reverse;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

public class CoreStreamUtils implements StreamUtils {
    @Override
    public <T> Collector<T, ?, Optional<List<T>>> toOptionalList() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                Optional::of
        );
    }

    @Override
    public <T> Collector<T, ?, Optional<T>> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                this::toSingletonList
        );
    }

    private <T> Optional<T> toSingletonList(List<T> list) {
        return Optional.of(list)
                .filter(this::isSingletonList)
                .map(singletonList -> singletonList.get(0));
    }

    private <T> boolean isSingletonList(List<T> list) {
        return list.size() == 1;
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

    @Override
    public <T> Stream<T> iterableToStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    @Override
    public <T> List<T> iterableToList(Iterable<T> iterable) {
        return iterableToStream(iterable).toList();
    }

    @Override
    public <T> Stream<T> filterStream(Collection<Predicate<T>> predicates, Stream<T> stream) {
        return stream.filter(composePredicate(predicates));
    }

    @Override
    public <T> Predicate<T> composePredicate(Collection<Predicate<T>> predicates) {
        Predicate<T> result = null;
        for (Predicate<T> predicate : predicates) {
            result = composePredicate(result, predicate);
        }
        return result;
    }

    @Override
    public <T> Predicate<T> composePredicate(Predicate<T> first, Predicate<T> second) {
        if (allNotNull(first, second)) {
            return first.and(second);
        } else {
            return Optional.ofNullable(firstNonNull(first, second))
                    .orElseThrow(NullPointerException::new);
        }
    }
}