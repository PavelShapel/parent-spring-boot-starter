package com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.RandomStarterAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = {
        RandomStarterAutoConfiguration.class,
        CoreStarterAutoConfiguration.class
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