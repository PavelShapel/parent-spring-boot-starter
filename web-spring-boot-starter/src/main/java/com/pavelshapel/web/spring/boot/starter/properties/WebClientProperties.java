package com.pavelshapel.web.spring.boot.starter.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebClientProperties {
    String baseUrl;
    Integer timeout = 1000;
}