package com.pavelshapel.stream.spring.boot.starter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.pavelshapel.stream.spring.boot.starter.StarterAutoConfiguration.PREFIX;

@Getter
@Setter
@ConfigurationProperties(PREFIX)
public class StreamProperties {
    private boolean streamUtils;
}