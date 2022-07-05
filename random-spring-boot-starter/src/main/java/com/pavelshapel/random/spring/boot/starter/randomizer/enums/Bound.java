package com.pavelshapel.random.spring.boot.starter.randomizer.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Getter
@ToString
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Bound {
    DEFAULT_MIN_YEAR(1000),
    DEFAULT_MAX_YEAR(3000),

    DEFAULT_MIN_LONG(new GregorianCalendar(
            Math.toIntExact(DEFAULT_MIN_YEAR.getValue()),
            Calendar.JANUARY,
            1).getTimeInMillis()
    ),
    DEFAULT_MAX_LONG(new GregorianCalendar(
            Math.toIntExact(DEFAULT_MAX_YEAR.getValue()),
            Calendar.DECEMBER,
            31).getTimeInMillis()
    ),

    DEFAULT_MIN_NATURAL(1),
    DEFAULT_MAX_NATURAL(Byte.MAX_VALUE);

    long value;
}
