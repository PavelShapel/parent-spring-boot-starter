package com.pavelshapel.core.spring.boot.starter.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum PrimitiveType {
    BOOLEAN(PrimitiveType::getRandomBoolean, PrimitiveType::castToBoolean),
    STRING(PrimitiveType::getRandomString, PrimitiveType::castToString),
    LONG(PrimitiveType::getRandomLong, PrimitiveType::castToLong),
    INTEGER(PrimitiveType::getRandomInteger, PrimitiveType::castToInteger),
    DOUBLE(PrimitiveType::getRandomDouble, PrimitiveType::castToDouble),
    DATE(PrimitiveType::getRandomDate, PrimitiveType::castToDate);

    Supplier<Object> randomValueSupplier;
    Function<String, Comparable<?>> castFunction;

    private static Date getRandomDate() {
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

    private static boolean getRandomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    private static Comparable<?> castToBoolean(String source) {
        return Boolean.valueOf(source);
    }

    private static Comparable<?> castToString(String source) {
        return source;
    }

    private static Comparable<?> castToLong(String source) {
        return Long.valueOf(source);
    }

    private static Comparable<?> castToInteger(String source) {
        return Integer.valueOf(source);
    }

    private static Comparable<?> castToDouble(String source) {
        return Double.valueOf(source);
    }

    private static Comparable<?> castToDate(String source) {
        return Date.from(LocalDate.parse(source)
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()
        );
    }
}
