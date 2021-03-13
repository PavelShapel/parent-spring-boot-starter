package com.pavelshapel.random.spring.boot.starter.randomizer.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Getter
@ToString
@RequiredArgsConstructor
public enum ConstantsRange {
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

    private final long value;
}
