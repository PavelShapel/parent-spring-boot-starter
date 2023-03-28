package com.pavelshapel.core.spring.boot.starter;

import com.pavelshapel.core.spring.boot.starter.impl.concurrent.AbstractForkJoinTask;

import java.util.List;

public class ConcurrentTester extends AbstractForkJoinTask<Integer> {
    public ConcurrentTester(List<Integer> payload) {
        super(payload);
    }

    public ConcurrentTester(List<Integer> payload, Integer threshold) {
        super(payload, threshold);
    }

    @Override
    protected Integer process(List<Integer> payload) {
        return payload.stream()
                .mapToInt(value -> value)
                .sum();
    }
}
