package com.pavelshapel.core.spring.boot.starter.util.impl;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.util.CompletableFutureUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {CoreStarterAutoConfiguration.class})
class CoreCompletableFutureUtilsTest {
    @Autowired
    private CompletableFutureUtils completableFutureUtils;

    @Test
    void allOf_WithValidParameter_ShouldReturnResult() {
        List<String> strings = generateStrings();
        List<CompletableFuture<String>> completableFutures = generateCompletableFutures(strings);

        CompletableFuture<List<String>> allOf = completableFutureUtils.allOf(completableFutures);

        assertThat(allOf.join())
                .asList()
                .hasSameSizeAs(completableFutures)
                .hasSameElementsAs(strings);
    }

    @Test
    void allOf_WithThrowableParameter_ShouldThrowException() {
        List<String> strings = generateStrings();
        List<CompletableFuture<String>> completableFutures = generateCompletableFutures(strings);
        CompletableFuture<String> exceptionableFuture = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException();
        });
        completableFutures.add(exceptionableFuture);

        CompletableFuture<List<String>> allOf = completableFutureUtils.allOf(completableFutures);

        assertThatThrownBy(allOf::join)
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void anyOf_WithValidParameter_ShouldReturnSecondFuture() throws ExecutionException, InterruptedException {
        String string1 = createString();
        String string2 = createString();
        String string3 = createString();
        CompletableFuture<String> future1 = createCompletableFuture(string1, 2);
        CompletableFuture<String> future2 = createCompletableFuture(string2, 1);
        CompletableFuture<String> future3 = createCompletableFuture(string3, 3);
        List<CompletableFuture<String>> completableFutures = Arrays.asList(future1, future2, future3);

        CompletableFuture<String> anyOf = completableFutureUtils.anyOf(completableFutures);

        assertThat(anyOf.get())
                .isEqualTo(string2);
    }

    private List<String> generateStrings() {
        return Stream.generate(this::createString)
                .limit(ThreadLocalRandom.current().nextInt(Byte.MAX_VALUE))
                .collect(Collectors.toList());
    }

    private List<CompletableFuture<String>> generateCompletableFutures(Collection<String> strings) {
        return strings.stream()
                .map(this::createCompletableFuture)
                .collect(Collectors.toList());
    }

    private CompletableFuture<String> createCompletableFuture(String string) {
        return createCompletableFuture(string, 0);
    }

    private CompletableFuture<String> createCompletableFuture(String string, long timeout) {
        return CompletableFuture.supplyAsync(() -> createString(string, timeout));
    }

    private String createString() {
        return createString(null, 0);
    }

    private String createString(String string, long timeout) {
        sleep(timeout);
        return Optional.ofNullable(string)
                .orElseGet(() -> RandomStringUtils.randomAlphanumeric(1, Byte.MAX_VALUE));
    }

    @SneakyThrows
    private void sleep(long timeout) {
        TimeUnit.SECONDS.sleep(timeout);
    }
}