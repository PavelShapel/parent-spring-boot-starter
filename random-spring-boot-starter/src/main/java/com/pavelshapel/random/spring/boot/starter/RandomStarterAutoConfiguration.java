package com.pavelshapel.random.spring.boot.starter;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.BoundedTypeBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.impl.BooleanBoundedType;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.impl.DateBoundedType;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.impl.DoubleBoundedType;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.impl.LongBoundedType;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.impl.StringBoundedType;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.RandomizerBeansCollection;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.collection.impl.GenericCollectionRandomizer;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.factory.impl.GenericRandomizerFactory;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl.BooleanRandomizer;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl.DateRandomizer;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl.DoubleRandomizer;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl.LongRandomizer;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl.StringRandomizer;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.verifier.SpecificationVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RandomStarterAutoConfiguration {
    public static final String TYPE = "random";

    @Bean
    public RandomContextRefreshedListener randomContextRefreshedListener() {
        return new RandomContextRefreshedListener();
    }

    @Bean
    public StringBoundedType stringBoundedType() {
        return new StringBoundedType();
    }

    @Bean
    public LongBoundedType longBoundedType() {
        return new LongBoundedType();
    }

    @Bean
    public DateBoundedType dateBoundedType() {
        return new DateBoundedType();
    }

    @Bean
    public BooleanBoundedType booleanBoundedType() {
        return new BooleanBoundedType();
    }

    @Bean
    public DoubleBoundedType doubleBoundedType() {
        return new DoubleBoundedType();
    }

    @Bean
    public BoundedTypeBeansCollection boundedBeansCollection() {
        return new BoundedTypeBeansCollection();
    }


    @Bean
    public LongRandomizer longRandomizer() {
        return new LongRandomizer();
    }

    @Bean
    public StringRandomizer stringRandomizer() {
        return new StringRandomizer();
    }

    @Bean
    public DoubleRandomizer doubleRandomizer() {
        return new DoubleRandomizer();
    }

    @Bean
    public BooleanRandomizer booleanRandomizer() {
        return new BooleanRandomizer();
    }

    @Bean
    public DateRandomizer dateRandomizer() {
        return new DateRandomizer();
    }

    @Bean
    public RandomizerBeansCollection randomizerBeansCollection() {
        return new RandomizerBeansCollection();
    }


    @Bean
    public SpecificationVerifier specificationVerifier() {
        return new SpecificationVerifier();
    }

    @Bean
    public GenericRandomizerFactory genericRandomizerFactory() {
        return new GenericRandomizerFactory();
    }

    @Bean
    public GenericCollectionRandomizer genericCollectionRandomizer() {
        return new GenericCollectionRandomizer();
    }
}