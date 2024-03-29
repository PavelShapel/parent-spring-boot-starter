package com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.BoundedType;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.verifier.SpecificationVerifier;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractRandomizer<T> implements Randomizer<T> {
    @Autowired
    BoundedType<T> boundedType;
    @Autowired
    SpecificationVerifier specificationVerifier;

    @Override
    public T randomize(Specification specification) {
        return rawRandomize(specificationVerifier.verify(specification));
    }

    protected abstract T rawRandomize(Specification specification);

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
        return createBoundedSpecification(boundedType.getRange().getMinimum(), boundedType.getRange().getMaximum());
    }


    private Specification createBoundedSpecification(long minValue, long maxValue) {
        Specification specification = Specification.builder(boundedType.getType().getSimpleName())
                .min(minValue)
                .max(maxValue)
                .build();
        return specificationVerifier.verify(specification);
    }
}
