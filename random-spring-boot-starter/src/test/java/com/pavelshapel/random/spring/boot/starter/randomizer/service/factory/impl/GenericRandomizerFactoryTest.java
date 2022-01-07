package com.pavelshapel.random.spring.boot.starter.randomizer.service.factory.impl;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.RandomStarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.RandomizerBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.Randomizer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        Assertions.assertThat(genericRandomizerFactory).isNotNull();
        Assertions.assertThat(randomizerBeansCollection).isNotNull();
    }

    @Test
    void getRandomizer_SpecificationAsParam_ShouldReturnRandomizer() {
        randomizerBeansCollection.getBeans().values().stream()
                .map(Randomizer::createDefaultSpecification)
                .forEach(specification -> assertThat(genericRandomizerFactory.getRandomizer(specification)).isNotNull());
    }
}