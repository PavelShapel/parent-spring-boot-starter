package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.api.util.MathUtils;
import com.pavelshapel.core.spring.boot.starter.impl.model.properties.NumberProperties;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.MapContext;

import java.math.BigDecimal;
import java.util.HashMap;

public class CoreMathUtils implements MathUtils {
    @Override
    public BigDecimal evaluate(String rawExpression, NumberProperties variables) {
        var jexlEngine = new JexlBuilder().create();
        var expression = jexlEngine.createExpression(rawExpression);
        var context = new MapContext(new HashMap<>(variables));
        return new BigDecimal(expression.evaluate(context).toString());
    }

}
