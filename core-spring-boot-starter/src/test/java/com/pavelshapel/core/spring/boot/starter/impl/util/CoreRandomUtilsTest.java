package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.EmptyEnumTester;
import com.pavelshapel.core.spring.boot.starter.EnumTester;
import com.pavelshapel.core.spring.boot.starter.api.util.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {CoreStarterAutoConfiguration.class})
class CoreRandomUtilsTest {
    @Autowired
    private RandomUtils randomUtils;

    @Test
    void getRandomisedEnum_WithValidParameter_ShouldReturnRandomValue() {
        Optional<EnumTester> randomisedEnum = randomUtils.getRandomisedEnum(EnumTester.class);

        assertThat(randomisedEnum)
                .isNotEmpty();
    }

    @Test
    void getRandomisedEnum_WithInvalidParameter_ShouldReturnEmptyValue() {
        Optional<EmptyEnumTester> randomisedEnum = randomUtils.getRandomisedEnum(EmptyEnumTester.class);

        assertThat(randomisedEnum)
                .isEmpty();
    }
}