package com.pavelshapel.core.spring.boot.starter.api.util;

import com.pavelshapel.core.spring.boot.starter.impl.model.properties.NumberProperties;

import java.math.BigDecimal;

public interface MathUtils {
    BigDecimal evaluate(String rawExpression, NumberProperties variables);
}
