package com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl;

import com.pavelshapel.random.spring.boot.starter.RandomStarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.Randomizer;
import com.pavelshapel.stream.spring.boot.starter.util.StreamUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {
        RandomStarterAutoConfiguration.class,
        StreamUtils.class
})
abstract class AbstractSingletonRandomizerTest<T> {
    @Autowired
    private Randomizer<T> randomizer;

    @Test
    void initialization() {
        Assertions.assertThat(randomizer).isNotNull();
    }

    @Test
    void randomize_WithoutParams_ShouldReturnRandomizedValue() {
        final T randomizedValue = randomizer.randomize();

        assertThat(randomizedValue).isNotNull();
    }

    @Test
    void randomize_WithLongParams_ShouldReturnRandomizedValue() {
        long min = ThreadLocalRandom.current().nextLong();
        long max = ThreadLocalRandom.current().nextLong();
        final T randomizedValue = randomizer.randomize(min, max);

        assertThat(randomizedValue).isNotNull();
    }
}