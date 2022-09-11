package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.api.util.ExceptionUtils;
import com.pavelshapel.core.spring.boot.starter.api.util.MathUtils;
import com.pavelshapel.core.spring.boot.starter.api.util.SubstitutionUtils;
import com.pavelshapel.core.spring.boot.starter.impl.model.properties.StringProperties;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.util.Optional;

import static java.util.Objects.nonNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoreMathUtils implements MathUtils {
    @Autowired
    ExceptionUtils exceptionUtils;
    @Autowired
    SubstitutionUtils substitutionUtils;

    @Override
    public BigDecimal evaluate(String rawExpression, StringProperties variables) {
        return Optional.ofNullable(rawExpression)
                .filter(unused -> nonNull(variables))
                .map(source -> replace(source, variables))
                .map(this::eval)
                .orElseThrow(() -> exceptionUtils.createIllegalArgumentException(
                        RAW_EXPRESSION, rawExpression,
                        VARIABLES, variables
                ));
    }

    private String replace(String source, StringProperties properties) {
        return substitutionUtils.replace(source, properties);
    }

    @SneakyThrows
    private BigDecimal eval(String expression) {
        return Optional.of(getEngine().eval(expression))
                .map(Object::toString)
                .map(NumberUtils::toScaledBigDecimal)
                .orElseThrow();
    }

    private ScriptEngine getEngine() {
        return new ScriptEngineManager().getEngineByName(JAVA_SCRIPT);
    }
}
