package com.pavelshapel.random.spring.boot.starter.randomizer.service.collection.impl;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.RandomStarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.randomizer.entity.Entity;
import com.pavelshapel.random.spring.boot.starter.randomizer.entity.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.BoundedTypeBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.RandomizerBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.Randomizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        RandomStarterAutoConfiguration.class,
        CoreStarterAutoConfiguration.class
})
class GenericCollectionRandomizerTest {
    @Autowired
    private GenericCollectionRandomizer genericCollectionRandomizer;
    @Autowired
    private BoundedTypeBeansCollection boundedTypeBeansCollection;
    @Autowired
    private RandomizerBeansCollection randomizerBeansCollection;

    @Test
    void initialization() {
        assertThat(genericCollectionRandomizer).isNotNull();
        assertThat(boundedTypeBeansCollection).isNotNull();
        assertThat(randomizerBeansCollection).isNotNull();
    }

    @Test
    void randomize_ListAsParam_ShouldReturnCollection() {
        List<Specification> specifications = randomizerBeansCollection.getBeans().values().stream()
                .map(Randomizer::createDefaultSpecification)
                .collect(Collectors.toList());

        Collection<Object> randomizedCollection = genericCollectionRandomizer
                .randomize(specifications);

        assertThat(randomizedCollection).hasSameSizeAs(boundedTypeBeansCollection.getBeans().values());
    }

    @Test
    void randomize_MapAsParam_ShouldReturnCollection() {
        Map<String, Specification> map = randomizerBeansCollection.getBeans().values().stream()
                .map(Randomizer::createDefaultSpecification)
                .collect(Collectors.toMap(
                        Object::toString,
                        specification -> specification)
                );

        Map<String, Object> randomizedMap = genericCollectionRandomizer.randomize(new Entity(map));

        assertThat(randomizedMap).hasSameSizeAs(boundedTypeBeansCollection.getBeans());
    }
}