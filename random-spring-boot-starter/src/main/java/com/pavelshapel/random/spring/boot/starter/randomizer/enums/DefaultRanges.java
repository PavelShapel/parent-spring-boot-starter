package com.pavelshapel.random.spring.boot.starter.randomizer.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.Range;

import static com.pavelshapel.random.spring.boot.starter.randomizer.enums.Bound.*;

@Getter
@ToString
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum DefaultRanges {
    DEFAULT_LONG_RANGE(
            Range.between(
                    DEFAULT_MIN_LONG.getValue(),
                    DEFAULT_MAX_LONG.getValue()
            )
    ),
    DEFAULT_NATURAL_RANGE(
            Range.between(
                    DEFAULT_MIN_NATURAL.getValue(),
                    DEFAULT_MAX_NATURAL.getValue()
            )
    ),
    DEFAULT_YEAR_RANGE(
            Range.between(
                    DEFAULT_MIN_YEAR.getValue(),
                    DEFAULT_MAX_YEAR.getValue()
            )
    );

    Range<Long> value;
}
