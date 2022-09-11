package com.pavelshapel.core.spring.boot.starter.api.util;

import com.pavelshapel.core.spring.boot.starter.impl.model.properties.NumberProperties;

import java.math.BigDecimal;

public interface MathUtils {
    String JAVA_SCRIPT = "JavaScript";
    String RAW_EXPRESSION = "rawExpression";
    String VARIABLES = "variables";

    BigDecimal evaluate(String rawExpression, NumberProperties variables);
}
