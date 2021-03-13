package com.pavelshapel.random.spring.boot.starter.randomizer.service.verifier;

import com.pavelshapel.random.spring.boot.starter.randomizer.entity.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.BoundedType;
import com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.BoundedTypeBeansCollection;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Predicate;

@Log4j2
public class SpecificationVerifier implements Verifier<Specification> {
    @Autowired
    private BoundedTypeBeansCollection boundedTypeBeansCollection;

    @Override
    public Specification verify(Specification specification) {
        final BoundedType<?> boundedType = getBoundedType(specification);

        try {
            final Range<Long> range = Range.between(specification.getMin(), specification.getMax());
            final Range<Long> intersection = boundedType.getRange().intersectionWith(range);

            return Specification.builder()
                    .type(specification.getType())
                    .min(intersection.getMinimum())
                    .max(intersection.getMaximum())
                    .build();
        } catch (Exception exception) {
            log.warn("implemented default range on exception [{}]", exception.toString());

            return Specification.builder()
                    .type(specification.getType())
                    .min(boundedType.getRange().getMinimum())
                    .max(boundedType.getRange().getMaximum())
                    .build();
        }
    }

    private BoundedType<?> getBoundedType(Specification specification) {
        return boundedTypeBeansCollection.getBean(getPredicate(specification))
                .orElseThrow(() -> new IllegalArgumentException(specification.toString()));
    }

    private Predicate<BoundedType<?>> getPredicate(Specification specification) {
        return boundedType -> boundedType.getType().getSimpleName().equalsIgnoreCase(specification.getType());
    }
}
