package com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.model.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.AbstractRandomizer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

import static com.pavelshapel.random.spring.boot.starter.randomizer.enums.DefaultRanges.DEFAULT_NATURAL_RANGE;

public class DoubleRandomizer extends AbstractRandomizer<Double> {
    @Override
    public Double randomize(Specification specification) {
        double randomizedDouble = ThreadLocalRandom.current().nextDouble(
                specification.getMin(),
                specification.getMax()
        );
        BigDecimal scaledBigDecimal = BigDecimal.valueOf(randomizedDouble).setScale(
                getRandomizedScale(),
                RoundingMode.HALF_UP
        );
        return scaledBigDecimal.doubleValue();
    }

    private int getRandomizedScale() {
        return ThreadLocalRandom.current().nextInt(
                DEFAULT_NATURAL_RANGE.getValue().getMinimum().intValue(),
                DEFAULT_NATURAL_RANGE.getValue().getMaximum().intValue()
        );
    }
}
