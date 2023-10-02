package com.pavelshapel.core.spring.boot.starter.impl.concurrent;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public abstract class AbstractForkJoinTask<T> extends RecursiveTask<T> {

    private final transient List<T> payload;

    private final Integer threshold;

    protected AbstractForkJoinTask(List<T> payload) {
        this(payload, 2);
    }

    protected AbstractForkJoinTask(List<T> payload, Integer threshold) {
        this.payload = payload;
        this.threshold = threshold;
    }

    @Override
    protected T compute() {
        if (payload.size() > threshold) {
            List<T> subResults = ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .map(ForkJoinTask::join)
                    .toList();
            return process(subResults);
        } else {
            return process(payload);
        }
    }

    private List<AbstractForkJoinTask<T>> createSubtasks() {
        List<AbstractForkJoinTask<T>> dividedTasks = new ArrayList<>();
        List<T> left = payload.subList(0, payload.size() / 2);
        List<T> right = new ArrayList<>(payload);
        right.removeAll(left);
        dividedTasks.add(createNewInstance(left));
        dividedTasks.add(createNewInstance(right));
        return dividedTasks;
    }

    @SneakyThrows
    private AbstractForkJoinTask<T> createNewInstance(List<T> payload) {
        return this.getClass().getDeclaredConstructor(List.class, Integer.class).newInstance(payload, threshold);
    }

    protected abstract T process(List<T> payload);
}
