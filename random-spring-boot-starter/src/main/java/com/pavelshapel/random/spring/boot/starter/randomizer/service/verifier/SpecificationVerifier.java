package com.pavelshapel.random.spring.boot.starter.randomizer.service.verifier;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.BoundedType;
import com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded.BoundedTypeBeansCollection;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Predicate;
import java.util.logging.Level;

@Log
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpecificationVerifier implements Verifier<Specification> {
    @Autowired
    BoundedTypeBeansCollection boundedTypeBeansCollection;

    @Override
    public Specification verify(Specification specification) {
        BoundedType<?> boundedType = getBoundedType(specification);
        try {
            Range<Long> range = Range.between(specification.getMin(), specification.getMax());
            Range<Long> intersection = boundedType.getRange().intersectionWith(range);
            return createSpecification(specification, intersection);
        } catch (Exception exception) {
            log.log(Level.WARNING, "implemented default range on exception [{}]", exception.toString());
            return createSpecification(specification, boundedType.getRange());
        }
    }

    private Specification createSpecification(Specification specification, Range<Long> range) {
        return Specification.builder()
                .type(specification.getType())
                .min(range.getMinimum())
                .max(range.getMaximum())
                .build();
    }

    private BoundedType<?> getBoundedType(Specification specification) {
        return boundedTypeBeansCollection.getBean(isSpecificationTypeEqualsBoundedType(specification))
                .orElseThrow(() -> new NotImplementedException(String.format("bounded type not implemented for [%s]", specification.toString())));
    }

    private Predicate<BoundedType<?>> isSpecificationTypeEqualsBoundedType(Specification specification) {
        return boundedType -> specification.getType().equalsIgnoreCase(boundedType.getType().getSimpleName());
    }
}
