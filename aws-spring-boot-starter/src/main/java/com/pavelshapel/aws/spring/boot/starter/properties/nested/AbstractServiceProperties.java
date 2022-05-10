package com.pavelshapel.aws.spring.boot.starter.properties.nested;

import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties.SERVICE_ENDPOINT_PATTERN;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractServiceProperties {
    final String name;
    @Setter
    String object;
    @Setter
    String endpoint = String.format(SERVICE_ENDPOINT_PATTERN, getName());
    @Setter
    Boolean enabled;
}
