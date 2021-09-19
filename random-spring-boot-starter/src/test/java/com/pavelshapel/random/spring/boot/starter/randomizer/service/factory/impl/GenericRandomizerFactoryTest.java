package com.pavelshapel.random.spring.boot.starter.randomizer.service.factory.impl;

import com.pavelshapel.random.spring.boot.starter.RandomStarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.RandomizerBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.Randomizer;
import com.pavelshapel.stream.spring.boot.starter.util.StreamUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {
        RandomStarterAutoConfiguration.class,
        StreamUtils.class
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