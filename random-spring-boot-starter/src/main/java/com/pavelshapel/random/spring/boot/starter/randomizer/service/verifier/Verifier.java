package com.pavelshapel.random.spring.boot.starter.randomizer.service.verifier;

@FunctionalInterface
public interface Verifier<T> {
    T verify(T object);
}