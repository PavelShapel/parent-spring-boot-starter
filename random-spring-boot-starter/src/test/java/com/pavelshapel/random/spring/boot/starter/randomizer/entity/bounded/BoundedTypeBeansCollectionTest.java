package com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded;

import com.pavelshapel.random.spring.boot.starter.StarterAutoConfiguration;
import com.pavelshapel.stream.spring.boot.starter.util.StreamUtils;
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
class BoundedTypeBeansCollectionTest {
    @Autowired
    private BoundedTypeBeansCollection boundedTypeBeansCollection;

    @Test
    void getBeans_WithoutParams_ShouldReturnBeansCollection() {
        assertThat(boundedTypeBeansCollection).isNotNull();
        assertThat(boundedTypeBeansCollection.getBeans()).isNotEmpty();
    }

    @Test
    void getBean_WithValidStringParam_ShouldReturnBean() {
        boundedTypeBeansCollection.getBeans().keySet()
                .forEach(beanName -> assertThat(boundedTypeBeansCollection.getBean(beanName)).isNotEmpty());
    }

    @Test
    void getBean_WithValidClassParam_ShouldReturnBean() {
        boundedTypeBeansCollection.getBeans().values()
                .forEach(bean -> assertThat(boundedTypeBeansCollection.getBean(bean.getClass())).isNotEmpty());
    }
}