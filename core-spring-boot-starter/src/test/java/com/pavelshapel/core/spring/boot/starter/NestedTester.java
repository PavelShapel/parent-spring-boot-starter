package com.pavelshapel.core.spring.boot.starter;

import java.util.function.Supplier;

public class NestedTester<T> implements Supplier<T> {
    @Override
    public T get() {
        return null;
    }
}