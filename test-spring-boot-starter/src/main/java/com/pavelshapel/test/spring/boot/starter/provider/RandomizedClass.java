package com.pavelshapel.test.spring.boot.starter.provider;

import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public enum RandomizedClass {
    BOOLEAN(RandomizedClass::getRandomBoolean),
    STRING(RandomizedClass::getRandomString),
    LONG(RandomizedClass::getRandomLong),
    INTEGER(RandomizedClass::getRandomInteger),
    DOUBLE(RandomizedClass::getRandomDouble),
    DATE(RandomizedClass::fetRandomDate);

    @Getter
    private final Supplier<Object> randomValueSupplier;

    RandomizedClass(Supplier<Object> randomValueSupplier) {
        this.randomValueSupplier = randomValueSupplier;
    }

    private static Date fetRandomDate() {
        Calendar min = new GregorianCalendar(1900, Calendar.JANUARY, 1);
        Calendar max = new GregorianCalendar(3000, Calendar.DECEMBER, 31);
        final long randomizedLong = ThreadLocalRandom.current().nextLong(
                min.getTimeInMillis(),
                max.getTimeInMillis()
        );
        return new Date(randomizedLong);
    }

    private static double getRandomDouble() {
        return ThreadLocalRandom.current().nextDouble(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    private static int getRandomInteger() {
        return ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static long getRandomLong() {
        return ThreadLocalRandom.current().nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private static String getRandomString() {
        return RandomStringUtils.randomAlphanumeric(1, Byte.MAX_VALUE);
    }

    private static Object getRandomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
