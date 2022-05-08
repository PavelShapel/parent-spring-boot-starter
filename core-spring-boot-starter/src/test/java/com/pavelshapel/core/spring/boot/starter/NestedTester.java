package com.pavelshapel.core.spring.boot.starter;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.function.Supplier;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NestedTester<T> implements Supplier<T> {
    String nestedName;

    @Override
    public T get() {
        return null;
    }
}