package com.pavelshapel.kafka.spring.boot.starter.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsumerKafkaProperties {
    String groupId;
    String autoOffsetReset = "latest";
}
