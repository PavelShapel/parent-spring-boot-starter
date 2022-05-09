package com.pavelshapel.aws.spring.boot.starter.properties.nested;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractServiceProperties {
    public static final String SERVICE_ENDPOINT_PATTERN = "https://%s.%s.amazonaws.com/";

    final String name;
    @Setter
    String object;
}
