package com.pavelshapel.core.spring.boot.starter.impl.web.search;

import com.pavelshapel.core.spring.boot.starter.enums.PrimitiveType;
import lombok.Data;
import org.springframework.data.util.Pair;

import java.util.Optional;

@Data
public class SearchCriteria {
    public static final String SPLIT_REGEX = "[,.:;]";

    private String field;
    private String value;
    private SearchOperation operation;

    public Comparable<?> getCastedValue() {
        return Optional.of(value)
                .map(source -> source.split(SPLIT_REGEX))
                .filter(strings -> strings.length == 2)
                .map(strings -> Pair.of(strings[0], PrimitiveType.valueOf(strings[1])))
                .map(this::castToTargetClass)
                .orElseThrow(() -> new UnsupportedOperationException(String.format("value [%s] unsupported", value)));
    }

    private Comparable<?> castToTargetClass(Pair<String, PrimitiveType> pair) {
        return pair.getSecond().getCastFunction().apply(pair.getFirst());
    }
}