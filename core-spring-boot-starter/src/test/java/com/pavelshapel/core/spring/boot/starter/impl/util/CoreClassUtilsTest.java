package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.Tester;
import com.pavelshapel.core.spring.boot.starter.api.util.ClassUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {CoreStarterAutoConfiguration.class})
class CoreClassUtilsTest {
    @Autowired
    private ClassUtils classUtils;

    @Test
    void getGenericSuperclass_WithValidParams_ShouldReturnResult() {
        Optional<Class<?>> genericSuperclass = classUtils.getGenericSuperclass(Tester.class);

        assertThat(genericSuperclass)
                .isNotEmpty()
                .hasValue(String.class);
    }

    @Test
    void getGenericSuperclass_WithInvalidParams_ShouldReturnEmptyValue() {
        Optional<Class<?>> genericSuperclass = classUtils.getGenericSuperclass(Tester.class, 1);

        assertThat(genericSuperclass)
                .isEmpty();
    }

    @Test
    void copyFields_WithFullSourceAndEmptyDestination_ShouldCopyAllFields() {
        Tester source = new Tester();
        source.setName(getRandomString());
        source.setNestedName(getRandomString());
        source.setSize(getRandomLong());
        Tester destination = new Tester();

        classUtils.copyFields(source, destination);

        assertThat(source).isEqualTo(destination);
    }

    @Test
    void copyFields_WithNullFieldInSourceAndFullDestination_ShouldCopyAllFields() {
        Tester source = new Tester();
        source.setName(getRandomString());
        source.setSize(getRandomLong());
        Tester destination = new Tester();
        destination.setName(getRandomString());
        destination.setNestedName(getRandomString());
        destination.setSize(getRandomLong());

        classUtils.copyFields(source, destination);

        assertThat(source.getName()).isEqualTo(destination.getName());
        assertThat(source.getSize()).isEqualTo(destination.getSize());
        assertThat(source.getNestedName()).isNotEqualTo(destination.getNestedName());
    }

    private long getRandomLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    private String getRandomString() {
        return RandomStringUtils.randomAlphabetic(Byte.MAX_VALUE);
    }
}