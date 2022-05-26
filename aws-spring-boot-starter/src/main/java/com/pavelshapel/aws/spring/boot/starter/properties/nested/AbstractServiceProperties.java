package com.pavelshapel.aws.spring.boot.starter.properties.nested;

import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties.SERVICE_ENDPOINT_PATTERN;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode
public abstract class AbstractServiceProperties {
    final String name;
    @Setter
    String object;
    @Setter
    String endpoint;
    @Setter
    Boolean enabled;

    protected AbstractServiceProperties(String name) {
        this.name = name;
        this.endpoint = String.format(SERVICE_ENDPOINT_PATTERN, name);
    }
}
