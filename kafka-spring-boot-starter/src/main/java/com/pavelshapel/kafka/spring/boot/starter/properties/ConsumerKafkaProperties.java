package com.pavelshapel.kafka.spring.boot.starter.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsumerKafkaProperties {
    String groupId = RandomStringUtils.randomAlphanumeric(1, Byte.MAX_VALUE);
    String autoOffsetReset = "latest";
    boolean enableAutoCommit = true;
}
