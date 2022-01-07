package com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.impl;

import com.pavelshapel.random.spring.boot.starter.randomizer.entity.Specification;
import com.pavelshapel.random.spring.boot.starter.randomizer.service.singleton.AbstractRandomizer;
import org.apache.commons.lang3.Range;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ThreadLocalRandom;

public class DateRandomizer extends AbstractRandomizer<Date> {
    @Override
    public Date randomize(Specification specification) {
        Range<Calendar> dateRange = getDateRange(specification);
        long randomizedLong = ThreadLocalRandom.current().nextLong(
                dateRange.getMinimum().getTimeInMillis(),
                dateRange.getMaximum().getTimeInMillis()
        );
        return new Date(randomizedLong);
    }

    private Range<Calendar> getDateRange(Specification specification) {
        Calendar min = new GregorianCalendar(
                Math.toIntExact(specification.getMin()),
                Calendar.JANUARY,
                1);
        Calendar max = new GregorianCalendar(
                Math.toIntExact(specification.getMax()),
                Calendar.DECEMBER,
                31);
        return Range.between(min, max);
    }
}
