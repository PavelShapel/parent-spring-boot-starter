package com.pavelshapel.aop.spring.boot.starter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.pavelshapel.aop.spring.boot.starter.StarterAutoConfiguration.PREFIX;

@Getter
@Setter
@ConfigurationProperties(PREFIX)
public class AopProperties {
    private boolean logMethodResult;
    private boolean logMethodDuration;
}