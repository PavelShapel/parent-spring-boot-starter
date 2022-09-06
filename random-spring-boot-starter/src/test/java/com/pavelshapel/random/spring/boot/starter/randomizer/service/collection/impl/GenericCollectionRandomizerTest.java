package com.pavelshapel.random.spring.boot.starter.randomizer.service.collection.impl;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.impl.bean.AbstractBeansCollection;
import com.pavelshapel.random.spring.boot.starter.RandomStarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.Entity;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.BoundedTypeBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.RandomizerBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.Randomizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

import static com.pavelshapel.random.spring.boot.starter.randomizer.service.collection.impl.GenericCollectionRandomizer.SET_CORRECT_ENTITY_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void randomize_ListAsParameter_ShouldReturnCollection() {
        List<Specification> specifications = Optional.of(randomizerBeansCollection)
                .map(AbstractBeansCollection::getBeans)
                .map(Map::values)
                .orElseGet(Collections::emptyList).stream()
                .map(Randomizer::createDefaultSpecification)
                .collect(Collectors.toList());


        Collection<Object> randomizedCollection = genericCollectionRandomizer
                .randomize(specifications);

        assertThat(randomizedCollection)
                .asList()
                .doesNotContainNull();
    }

    @Test
    void randomize_MapWithoutEntityAsParameter_ShouldReturnMap() {
        Map<String, Specification> map = Optional.of(randomizerBeansCollection)
                .map(AbstractBeansCollection::getBeans)
                .map(Map::values)
                .orElseGet(Collections::emptyList).stream()
                .map(Randomizer::createDefaultSpecification)
                .collect(Collectors.toMap(
                        Object::toString,
                        specification -> specification)
                );

        Map<String, Object> randomizedMap = genericCollectionRandomizer
                .randomize(new Entity(map));

        assertThat(randomizedMap)
                .doesNotContainValue(null);
    }

    @Test
    void randomize_MapWithEntityWithoutBodyAsParameter_ShouldThrowException() {
        Entity entity = Optional.of(Entity.class)
                .map(Class::getSimpleName)
                .map(className -> Specification.builder().type(className))
                .map(Specification.SpecificationBuilder::build)
                .map(specification -> Collections.singletonMap("", specification))
                .map(Entity::new)
                .orElseThrow();

        assertThatThrownBy(() -> genericCollectionRandomizer.randomize(entity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(SET_CORRECT_ENTITY_EXCEPTION);
    }
}