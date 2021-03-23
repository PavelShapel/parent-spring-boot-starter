package com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl;

import com.pavelshapel.random.spring.boot.starter.StarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.provider.Long2Provider;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.Randomizer;
import com.pavelshapel.stream.spring.boot.starter.util.StreamUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static com.pavelshapel.random.spring.boot.starter.StarterAutoConfiguration.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {
        StarterAutoConfiguration.class,
        StreamUtils.class
})
abstract class AbstractSingletonRandomizerTest<T> {
    @Autowired
    private Randomizer<T> randomizer;

    @Test
    void randomize_WithoutParams_ShouldReturnRandomizedValue() {
        final T randomizedValue = randomizer.randomize();

        assertThat(randomizedValue).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(Long2Provider.class)
    void randomize_WithLongParams_ShouldReturnRandomizedValue(long min, long max) {
        final T randomizedValue = randomizer.randomize(min, max);

        assertThat(randomizedValue).isNotNull();
    }
}