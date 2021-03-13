package com.pavelshapel.random.spring.boot.starter;

import com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.BoundedTypeBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.impl.*;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.RandomizerBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.collection.impl.GenericCollectionRandomizer;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.factory.impl.GenericRandomizerFactory;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl.*;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.verifier.SpecificationVerifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RandomProperties.class)
public class StarterAutoConfiguration {
    public static final String TYPE = "random";
    public static final String PREFIX = "spring.pavelshapel." + TYPE;
    public static final String PROPERTY_NAME = "randomizers";
    public static final String TRUE = "true";

    @Bean
    public RandomContextRefreshedListener randomContextRefreshedListener() {
        return new RandomContextRefreshedListener();
    }


    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public StringBoundedType stringBoundedType() {
        return new StringBoundedType();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public LongBoundedType longBoundedType() {
        return new LongBoundedType();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public DateBoundedType dateBoundedType() {
        return new DateBoundedType();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public BooleanBoundedType booleanBoundedType() {
        return new BooleanBoundedType();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public DoubleBoundedType doubleBoundedType() {
        return new DoubleBoundedType();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public BoundedTypeBeansCollection boundedBeansCollection() {
        return new BoundedTypeBeansCollection();
    }


    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public LongRandomizer longRandomizer() {
        return new LongRandomizer();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public StringRandomizer stringRandomizer() {
        return new StringRandomizer();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public DoubleRandomizer doubleRandomizer() {
        return new DoubleRandomizer();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public BooleanRandomizer booleanRandomizer() {
        return new BooleanRandomizer();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public DateRandomizer dateRandomizer() {
        return new DateRandomizer();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public RandomizerBeansCollection randomizerBeansCollection() {
        return new RandomizerBeansCollection();
    }


    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public SpecificationVerifier specificationVerifier() {
        return new SpecificationVerifier();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public GenericRandomizerFactory genericRandomizerFactory() {
        return new GenericRandomizerFactory();
    }

    @Bean
    @ConditionalOnProperty(prefix = PREFIX, name = PROPERTY_NAME, havingValue = TRUE)
    public GenericCollectionRandomizer genericCollectionRandomizer() {
        return new GenericCollectionRandomizer();
    }
}