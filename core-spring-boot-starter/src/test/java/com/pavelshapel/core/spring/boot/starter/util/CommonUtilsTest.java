package com.pavelshapel.core.spring.boot.starter.util;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {CoreStarterAutoConfiguration.class})
class CommonUtilsTest {
    @Autowired
    private CommonUtils commonUtils;

    @Test
    void getGenericSuperclass_WithValidParameter_ShouldReturnResult() {
        Optional<Class<?>> genericSuperclass = commonUtils.getGenericSuperclass(Tester.class);

        assertThat(genericSuperclass)
                .isNotEmpty()
                .hasValue(String.class);
    }

    @Test
    void getGenericSuperclass_WithInvalidParameter_ShouldReturnEmptyValue() {
        Optional<Class<?>> genericSuperclass = commonUtils.getGenericSuperclass(Tester.class, 1);

        assertThat(genericSuperclass)
                .isEmpty();
    }
}