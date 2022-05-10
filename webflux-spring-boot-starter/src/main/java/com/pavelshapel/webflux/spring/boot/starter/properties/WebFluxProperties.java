package com.pavelshapel.webflux.spring.boot.starter.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.pavelshapel.webflux.spring.boot.starter.WebFluxStarterAutoConfiguration.TYPE;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = WebFluxProperties.PREFIX)
public class WebFluxProperties {
    public static final String PREFIX = "spring." + TYPE;
    WebClientProperties webClient = new WebClientProperties();
}