package com.pavelshapel.kafka.spring.boot.starter.service;

import com.pavelshapel.web.spring.boot.starter.web.converter.AbstractDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface Kafka<T extends AbstractDto> {
    default String convertConsumerRecordToString(String prefix, ConsumerRecord<String, T> consumerRecord) {
        return convertConsumerRecordToString(prefix, consumerRecord.topic(), consumerRecord.key(), consumerRecord.value());
    }

    default String convertConsumerRecordToString(String prefix, String topic, String key, T value) {
        return String.format("%s [topic [%s], key [%s], value [%s]]", prefix, topic, key, value);
    }
}
