package com.pavelshapel.core.spring.boot.starter.impl.concurrent;

import com.pavelshapel.core.spring.boot.starter.ConcurrentTester;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractForkJoinTaskTest {

    @Test
    void process_WithValidParameter_ShouldReturnResult() {
        List<Integer> integers = ThreadLocalRandom.current().ints()
                .limit(Byte.MAX_VALUE)
                .boxed()
                .collect(Collectors.toList());
        int sum = integers.stream()
                .mapToInt(value -> value)
                .sum();
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        Integer result = forkJoinPool.invoke(new ConcurrentTester(integers));

        assertThat(result).isEqualTo(sum);
    }
}