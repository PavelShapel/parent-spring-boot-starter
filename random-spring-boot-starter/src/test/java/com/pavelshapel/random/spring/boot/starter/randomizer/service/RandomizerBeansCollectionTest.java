package com.pavelshapel.random.spring.boot.starter.randomizer.service;

import com.pavelshapel.random.spring.boot.starter.StarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.BoundedTypeBeansCollection;
import com.pavelshapel.stream.spring.boot.starter.util.StreamUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static com.pavelshapel.random.spring.boot.starter.StarterAutoConfiguration.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {
        StarterAutoConfiguration.class,
        StreamUtils.class
})
class RandomizerBeansCollectionTest {
    @Autowired
    private RandomizerBeansCollection randomizerBeansCollection;
    @Autowired
    private BoundedTypeBeansCollection boundedTypeBeansCollection;

    @Test
    void getBeans_WithoutParams_ShouldReturnBeansCollection() {
        Assertions.assertThat(randomizerBeansCollection).isNotNull();
        Assertions.assertThat(randomizerBeansCollection.getBeans())
                .isNotEmpty()
                .hasSameSizeAs(boundedTypeBeansCollection.getBeans());
    }

    @Test
    void getBean_WithValidStringParam_ShouldReturnBean() {
        randomizerBeansCollection.getBeans().keySet()
                .forEach(beanName -> assertThat(randomizerBeansCollection.getBean(beanName)).isNotEmpty());
    }

    @Test
    void getBean_WithValidClassParam_ShouldReturnBean() {
        randomizerBeansCollection.getBeans().values()
                .forEach(bean -> assertThat(randomizerBeansCollection.getBean(bean.getClass())).isNotEmpty());
    }
}