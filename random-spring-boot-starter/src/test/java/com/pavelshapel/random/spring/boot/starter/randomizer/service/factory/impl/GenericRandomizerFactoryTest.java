package com.pavelshapel.random.spring.boot.starter.randomizer.service.factory.impl;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.impl.bean.AbstractBeansCollection;
import com.pavelshapel.random.spring.boot.starter.RandomStarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.RandomizerBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.Randomizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {
        RandomStarterAutoConfiguration.class,
        CoreStarterAutoConfiguration.class
})
class GenericRandomizerFactoryTest {
    @Autowired
    private RandomizerBeansCollection randomizerBeansCollection;
    @Autowired
    private GenericRandomizerFactory genericRandomizerFactory;

    @Test
    void initialization() {
        assertThat(genericRandomizerFactory).isNotNull();
        assertThat(randomizerBeansCollection).isNotNull();
    }

    @Test
    void getRandomizer_SpecificationAsParameter_ShouldReturnRandomizer() {
        Optional.of(randomizerBeansCollection)
                .map(AbstractBeansCollection::getBeans)
                .map(Map::values)
                .orElseGet(Collections::emptyList).stream()
                .map(Randomizer::createDefaultSpecification)
                .forEach(specification ->
                        assertThat(genericRandomizerFactory.getRandomizer(specification)).isNotNull()
                );
    }
}