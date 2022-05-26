package com.pavelshapel.core.spring.boot.starter.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Instant;
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
    DATE(PrimitiveType::getRandomDate, PrimitiveType::castToDate),
    LOCAL_DATE(PrimitiveType::getRandomLocalDate, PrimitiveType::castToLocalDate);

    Supplier<?> randomValueSupplier;
    Function<String, Comparable<?>> castFunction;

    private static Date getRandomDate() {
        return new Date(getRandomizedMillis());
    }

    private static LocalDate getRandomLocalDate() {
        return Instant.ofEpochMilli(getRandomizedMillis())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private static long getRandomizedMillis() {
        Calendar min = new GregorianCalendar(1900, Calendar.JANUARY, 1);
        Calendar max = new GregorianCalendar(3000, Calendar.DECEMBER, 31);
        return ThreadLocalRandom.current().nextLong(
                min.getTimeInMillis(),
                max.getTimeInMillis()
        );
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

    private static Boolean castToBoolean(String source) {
        return Boolean.valueOf(source);
    }

    private static String castToString(String source) {
        return source;
    }

    private static Long castToLong(String source) {
        return Long.valueOf(source);
    }

    private static Integer castToInteger(String source) {
        return Integer.valueOf(source);
    }

    private static Double castToDouble(String source) {
        return Double.valueOf(source);
    }

    private static Date castToDate(String source) {
        return Date.from(LocalDate.parse(source)
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()
        );
    }

    private static LocalDate castToLocalDate(String source) {
        return LocalDate.parse(source);
    }
}
