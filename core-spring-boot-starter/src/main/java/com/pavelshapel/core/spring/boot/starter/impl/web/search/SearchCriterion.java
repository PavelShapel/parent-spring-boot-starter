package com.pavelshapel.core.spring.boot.starter.impl.web.search;

import com.pavelshapel.core.spring.boot.starter.enums.PrimitiveType;
import lombok.Data;
import org.springframework.data.util.Pair;

import java.util.Optional;

@Data
public class SearchCriterion {
    public static final String SPLIT_REGEX = "[,;]";

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
        return Optional.of(value)
                .map(source -> source.split(SPLIT_REGEX))
                .filter(strings -> strings.length == 2)
                .map(strings -> Pair.of(strings[0], PrimitiveType.valueOf(strings[1])))
                .orElseThrow(() -> new UnsupportedOperationException(String.format("value [%s] unsupported", value)));
    }

    private Comparable<?> castToTargetClass(Pair<String, PrimitiveType> pair) {
        return pair.getSecond().getCastFunction().apply(pair.getFirst());
    }
}