package com.pavelshapel.amqp.spring.boot.starter;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.pavelshapel.amqp.spring.boot.starter.StarterAutoConfiguration.PREFIX;
import static com.pavelshapel.amqp.spring.boot.starter.StarterAutoConfiguration.TYPE;

@Data
@ConfigurationProperties(prefix = PREFIX)
public class AmqpProperties {
    private String queue = String.format("%s-queue", TYPE);
    private String exchange = String.format("%s-exchange", TYPE);
    private String key = String.format("%s.key", TYPE);
}
