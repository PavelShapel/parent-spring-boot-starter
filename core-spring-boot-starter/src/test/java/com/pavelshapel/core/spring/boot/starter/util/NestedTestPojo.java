package com.pavelshapel.core.spring.boot.starter.util;

import java.util.function.Supplier;

public class NestedTestPojo<T> implements Supplier<T> {
    @Override
    public T get() {
        return null;
    }
}