package com.pavelshapel.core.spring.boot.starter.util.impl;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.util.ClassUtils;
import com.pavelshapel.core.spring.boot.starter.util.Tester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {CoreStarterAutoConfiguration.class})
class CoreClassUtilsTest {
    @Autowired
    private ClassUtils classUtils;

    @Test
    void getGenericSuperclass_WithValidParameter_ShouldReturnResult() {
        Optional<Class<?>> genericSuperclass = classUtils.getGenericSuperclass(Tester.class);

        assertThat(genericSuperclass)
                .isNotEmpty()
                .hasValue(String.class);
    }

    @Test
    void getGenericSuperclass_WithInvalidParameter_ShouldReturnEmptyValue() {
        Optional<Class<?>> genericSuperclass = classUtils.getGenericSuperclass(Tester.class, 1);

        assertThat(genericSuperclass)
                .isEmpty();
    }
}