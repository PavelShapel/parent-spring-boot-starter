package com.pavelshapel.core.spring.boot.starter.impl.web.search;

import com.pavelshapel.core.spring.boot.starter.enums.PrimitiveType;
import lombok.Data;
import org.springframework.data.util.Pair;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

import static com.pavelshapel.core.spring.boot.starter.impl.web.search.SearchOperation.*;

@Data
public class SearchCriteria {
    public static final String DEFAULT_FIELD = "id";
    public static final String SPLIT_REGEX = "[,.:;]";
    public static final String DEFAULT_VALUE = "0L,LONG";

    @NotBlank
    private String field = DEFAULT_FIELD;
    @NotBlank
    private String value = DEFAULT_VALUE;
    private SearchOperation operation = GREATER_THAN_OR_EQUAL_TO;

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