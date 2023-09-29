package com.pavelshapel.jpa.spring.boot.starter.service.search;

import com.pavelshapel.core.spring.boot.starter.enums.PrimitiveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.function.Predicate.not;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriterion {
    public static final String TYPE_PREFIX = "<";
    public static final String TYPE_SUFFIX = ">";
    public static final String TYPE_REGEX = "%s.+%s$".formatted(TYPE_PREFIX, TYPE_SUFFIX);

    private String field;
    private String value;
    private SearchOperation operation;

    public Comparable<?> getCastedValue() {
        return Optional.ofNullable(value)
                .map(this::buildPair)
                .map(this::castToTargetClass)
                .orElse(null);
    }

    private Pair<String, PrimitiveType> buildPair(String value) {
        Pattern pattern = Pattern.compile(TYPE_REGEX);
        Matcher matcher = pattern.matcher(value);
        String primitiveType = Optional.of(matcher)
                .filter(Matcher::find)
                .map(Matcher::group)
                .map(type -> type.replace(TYPE_PREFIX, EMPTY))
                .map(type -> type.replace(TYPE_SUFFIX, EMPTY))
                .orElseThrow(() -> new IllegalArgumentException("type not found [%s]".formatted(value)));
        String primitiveValue = Optional.of(value.replaceAll(TYPE_REGEX, EMPTY))
                .filter(not("null"::equals))
                .filter(StringUtils::hasText)
                .orElseThrow(() -> new IllegalArgumentException("invalid value [%s]".formatted(value)));
        return Pair.of(primitiveValue, PrimitiveType.valueOf(primitiveType));
    }

    private Comparable<?> castToTargetClass(Pair<String, PrimitiveType> pair) {
        return pair.getSecond().getCastFunction().apply(pair.getFirst());
    }
}