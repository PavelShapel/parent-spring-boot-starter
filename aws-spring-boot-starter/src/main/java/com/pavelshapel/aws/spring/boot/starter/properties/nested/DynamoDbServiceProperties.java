package com.pavelshapel.aws.spring.boot.starter.properties.nested;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyDynamoDb.DYNAMO_DB;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamoDbServiceProperties extends AbstractServiceProperties {
    public DynamoDbServiceProperties() {
        super(DYNAMO_DB);
    }
}
