package com.pavelshapel.random.spring.boot.starter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(StarterAutoConfiguration.PREFIX)
public class RandomProperties {
    private boolean randomizers;
}