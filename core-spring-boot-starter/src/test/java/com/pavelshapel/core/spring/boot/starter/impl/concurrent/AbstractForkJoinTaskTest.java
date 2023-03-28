package com.pavelshapel.core.spring.boot.starter.impl.concurrent;

import com.pavelshapel.core.spring.boot.starter.ConcurrentTester;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractForkJoinTaskTest {

    @Test
    void process_WithValidParameter_ShouldReturnResult() {
        List<Integer> integers = IntStream.range(0, 10).boxed().collect(Collectors.toList());
        int sum = integers.stream().mapToInt(value -> value).sum();
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        Integer result = forkJoinPool.invoke(new ConcurrentTester(integers));

        assertThat(result).isEqualTo(sum);
    }
}