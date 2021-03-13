package com.pavelshapel.random.spring.boot.starter.randomizer.service.verifier;

import com.pavelshapel.random.spring.boot.starter.StarterAutoConfiguration;
import com.pavelshapel.random.spring.boot.starter.randomizer.entity.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.RandomizerBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.Randomizer;
import com.pavelshapel.stream.spring.boot.starter.util.StreamUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static com.pavelshapel.random.spring.boot.starter.StarterAutoConfiguration.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = {
        PREFIX + "." + PROPERTY_NAME + "=" + TRUE
})
@ContextConfiguration(classes = {
        StarterAutoConfiguration.class,
        StreamUtils.class
})
class SpecificationVerifierTest {
    @Autowired
    private RandomizerBeansCollection randomizerBeansCollection;
    @Autowired
    private SpecificationVerifier specificationVerifier;

    @Test
    void verify_WithValidParam_ShouldReturnSpecification() {
        randomizerBeansCollection.getBeans().values().stream()
                .map(Randomizer::createDefaultSpecification)
                .forEach(specification -> assertThat(specificationVerifier.verify(specification))
                        .isNotNull()
                        .isInstanceOf(Specification.class));
    }
}