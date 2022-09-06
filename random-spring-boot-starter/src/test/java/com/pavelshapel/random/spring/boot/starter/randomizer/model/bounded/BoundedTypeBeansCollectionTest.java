package com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.impl.bean.AbstractBeansCollection;
import com.pavelshapel.random.spring.boot.starter.RandomStarterAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        RandomStarterAutoConfiguration.class,
        CoreStarterAutoConfiguration.class
})
class BoundedTypeBeansCollectionTest {
    @Autowired
    private BoundedTypeBeansCollection boundedTypeBeansCollection;

    @Test
    void initialization() {
        assertThat(boundedTypeBeansCollection).isNotNull();
        assertThat(boundedTypeBeansCollection.getBeans()).isNotEmpty();
    }

    @Test
    void getBean_WithValidStringParameter_ShouldReturnBean() {
        Optional.of(boundedTypeBeansCollection)
                .map(AbstractBeansCollection::getBeans)
                .map(Map::keySet)
                .orElseGet(Collections::emptySet)
                .forEach(beanName ->
                        assertThat(boundedTypeBeansCollection.getBean(beanName)).isNotEmpty()
                );
    }

    @Test
    void getBean_WithValidClassParameter_ShouldReturnBean() {
        Optional.of(boundedTypeBeansCollection)
                .map(AbstractBeansCollection::getBeans)
                .map(Map::values)
                .orElseGet(Collections::emptySet)
                .forEach(bean ->
                        assertThat(boundedTypeBeansCollection.getBean(bean.getClass())).isNotEmpty()
                );
    }
}