package com.pavelshapel.random.spring.boot.starter.randomizer.service.verifier;

import com.pavelshapel.random.spring.boot.starter.randomizer.entity.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.BoundedType;
import com.pavelshapel.random.spring.boot.starter.randomizer.entity.bounded.BoundedTypeBeansCollection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Predicate;

@Slf4j
public class SpecificationVerifier implements Verifier<Specification> {
    @Autowired
    private BoundedTypeBeansCollection boundedTypeBeansCollection;

    @Override
    public Specification verify(Specification specification) {
        BoundedType<?> boundedType = getBoundedType(specification);

        try {
            Range<Long> range = Range.between(specification.getMin(), specification.getMax());
            Range<Long> intersection = boundedType.getRange().intersectionWith(range);

            return Specification.builder()
                    .type(specification.getType())
                    .min(intersection.getMinimum())
                    .max(intersection.getMaximum())
                    .build();
        } catch (Exception exception) {
            log.warn("Implemented default range on exception [{}]", exception.toString());
            return Specification.builder()
                    .type(specification.getType())
                    .min(boundedType.getRange().getMinimum())
                    .max(boundedType.getRange().getMaximum())
                    .build();
        }
    }

    private BoundedType<?> getBoundedType(Specification specification) {
        return boundedTypeBeansCollection.getBean(isSpecificationTypeEqualsBoundedType(specification))
                .orElseThrow(() -> new IllegalArgumentException(specification.toString()));
    }

    private Predicate<BoundedType<?>> isSpecificationTypeEqualsBoundedType(Specification specification) {
        return boundedType -> specification.getType().equalsIgnoreCase(boundedType.getType().getSimpleName());
    }
}
