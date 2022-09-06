package com.pavelshapel.random.spring.boot.starter.randomizer.service.verifier;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.impl.bean.AbstractBeansCollection;
import com.pavelshapel.random.spring.boot.starter.RandomStarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.Specification;
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
class SpecificationVerifierTest {
    @Autowired
    private RandomizerBeansCollection randomizerBeansCollection;
    @Autowired
    private SpecificationVerifier specificationVerifier;

    @Test
    void verify_WithValidParameter_ShouldReturnSpecification() {
        Optional.of(randomizerBeansCollection)
                .map(AbstractBeansCollection::getBeans)
                .map(Map::values)
                .orElseGet(Collections::emptyList).stream()
                .map(Randomizer::createDefaultSpecification)
                .forEach(specification ->
                        assertThat(specificationVerifier.verify(specification))
                                .isNotNull()
                                .isInstanceOf(Specification.class)
                );
    }
}