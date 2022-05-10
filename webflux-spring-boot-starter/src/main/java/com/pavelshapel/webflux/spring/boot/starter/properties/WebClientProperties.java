package com.pavelshapel.webflux.spring.boot.starter.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebClientProperties {
    String baseUrl;
    int timeout = 1000;
    Boolean enabled;
}