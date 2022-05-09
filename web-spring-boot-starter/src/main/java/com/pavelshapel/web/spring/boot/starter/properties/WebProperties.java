package com.pavelshapel.web.spring.boot.starter.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.pavelshapel.web.spring.boot.starter.WebStarterAutoConfiguration.TYPE;
import static com.pavelshapel.web.spring.boot.starter.properties.WebProperties.PREFIX;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = PREFIX)
public class WebProperties {
    public static final String PREFIX = "spring." + TYPE;
    WebClientProperties webClient;
}