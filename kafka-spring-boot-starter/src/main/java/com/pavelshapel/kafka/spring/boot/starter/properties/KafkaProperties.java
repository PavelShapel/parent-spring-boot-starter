package com.pavelshapel.kafka.spring.boot.starter.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.pavelshapel.kafka.spring.boot.starter.KafkaStarterAutoConfiguration.TYPE;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "spring." + TYPE)
public class KafkaProperties {
    String server;
    ConsumerKafkaProperties consumer = new ConsumerKafkaProperties();
}
