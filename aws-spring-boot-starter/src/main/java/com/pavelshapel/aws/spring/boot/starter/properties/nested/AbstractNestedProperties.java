package com.pavelshapel.aws.spring.boot.starter.properties.nested;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractNestedProperties {
    boolean enable = false;
}
