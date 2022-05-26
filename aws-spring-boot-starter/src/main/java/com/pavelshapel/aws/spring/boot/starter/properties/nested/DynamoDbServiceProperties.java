package com.pavelshapel.aws.spring.boot.starter.properties.nested;

import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyDynamoDb.DYNAMO_DB;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DynamoDbServiceProperties extends AbstractServiceProperties {
    public static final String EVERY_MONTH_CRON = "0 0 1 * *";

    @Getter
    @Setter
    String backupCron;

    public DynamoDbServiceProperties() {
        super(DYNAMO_DB);
        this.backupCron = EVERY_MONTH_CRON;
    }
}
