package com.pavelshapel.aws.spring.boot.starter.properties.nested;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyLambda.LAMBDA;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LambdaServiceProperties extends AbstractServiceProperties {
    public LambdaServiceProperties() {
        super(LAMBDA);
    }
}
