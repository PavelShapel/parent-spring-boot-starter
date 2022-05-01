package com.pavelshapel.aws.spring.boot.starter.properties.nested;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import static com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties.DEFAULT_REGION;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DynamoDbNestedProperties extends AbstractNestedProperties {
    static final String DYNAMO_DB_URL_PATTERN = "https://dynamodb.%s.amazonaws.com/";

    String endpoint = String.format(DYNAMO_DB_URL_PATTERN, DEFAULT_REGION);
    String table;
}
