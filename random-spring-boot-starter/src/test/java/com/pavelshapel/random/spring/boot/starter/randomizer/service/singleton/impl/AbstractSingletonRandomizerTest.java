package com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.RandomStarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.Randomizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {
        RandomStarterAutoConfiguration.class,
        CoreStarterAutoConfiguration.class
})
abstract class AbstractSingletonRandomizerTest<T> {
    @Autowired
    private Randomizer<T> randomizer;

    @Test
    void initialization() {
        assertThat(randomizer).isNotNull();
    }

    @Test
    void randomize_WithoutParameters_ShouldReturnRandomizedValue() {
        T randomizedValue = randomizer.randomize();

        assertThat(randomizedValue).isNotNull();
    }

    @Test
    void randomize_WithLongParameters_ShouldReturnRandomizedValue() {
        long min = ThreadLocalRandom.current().nextLong();
        long max = ThreadLocalRandom.current().nextLong();

        T randomizedValue = randomizer.randomize(min, max);

        assertThat(randomizedValue).isNotNull();
    }
}