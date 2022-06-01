package com.pavelshapel.jpa.spring.boot.starter.service.search.predicate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class AbstractPredicate implements Predicate<Comparable<Object>> {
    Object pattern;
}
