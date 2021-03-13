package com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton;

import com.pavelshapel.random.spring.boot.starter.randomizer.entity.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.BoundedType;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.verifier.SpecificationVerifier;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRandomizer<T> implements Randomizer<T> {
    @Autowired
    private BoundedType<T> boundedType;
    @Autowired
    private SpecificationVerifier verifier;

    @Override
    public T randomize() {
        return randomize(createDefaultSpecification());
    }

    @Override
    public T randomize(long minValue, long maxValue) {
        return randomize(createBoundedSpecification(minValue, maxValue));
    }

    @Override
    public Specification createDefaultSpecification() {
        return Specification.builder()
                .type(boundedType.getType().getSimpleName())
                .min(boundedType.getRange().getMinimum())
                .max(boundedType.getRange().getMaximum())
                .build();
    }


    private Specification createBoundedSpecification(long minValue, long maxValue) {
        final Specification specification = Specification.builder()
                .type(boundedType.getType().getSimpleName())
                .min(minValue)
                .max(maxValue)
                .build();

        return verifier.verify(specification);
    }
}
