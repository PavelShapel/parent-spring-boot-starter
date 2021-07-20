package com.pavelshapel.test.spring.boot.starter.provider;

import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public enum RandomizedClass {
    BOOLEAN(() -> ThreadLocalRandom.current().nextBoolean()),
    STRING(() -> RandomStringUtils.randomAlphanumeric(1, Byte.MAX_VALUE)),
    LONG(() -> ThreadLocalRandom.current().nextLong(Long.MIN_VALUE, Long.MAX_VALUE)),
    INTEGER(() -> ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE)),
    DOUBLE(() -> ThreadLocalRandom.current().nextDouble(Double.MIN_VALUE, Double.MAX_VALUE)),
    DATE(() -> {
        Calendar min = new GregorianCalendar(1900, Calendar.JANUARY, 1);
        Calendar max = new GregorianCalendar(3000, Calendar.DECEMBER, 31);
        final long randomizedLong = ThreadLocalRandom.current().nextLong(
                min.getTimeInMillis(),
                max.getTimeInMillis()
        );
        return new Date(randomizedLong);
    });

    @Getter
    private final Supplier<Object> randomValueSupplier;

    RandomizedClass(Supplier<Object> randomValueSupplier) {
        this.randomValueSupplier = randomValueSupplier;
    }
}
