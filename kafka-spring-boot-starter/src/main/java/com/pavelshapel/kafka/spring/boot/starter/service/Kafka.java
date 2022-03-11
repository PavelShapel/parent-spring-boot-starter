package com.pavelshapel.kafka.spring.boot.starter.service;

import com.pavelshapel.core.spring.boot.starter.model.Dto;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Optional;

public interface Kafka<T extends Dto<String>> {
    default String convertConsumerRecordToString(String prefix, ConsumerRecord<String, T> consumerRecord) {
        return convertConsumerRecordToString(prefix, consumerRecord.topic(), consumerRecord.key(), consumerRecord.value());
    }

    default String convertConsumerRecordToString(String prefix, String topic, String key, T value) {
        return String.format("%s [topic [%s], key [%s], value [%s]]", prefix, topic, Optional.ofNullable(key).orElse("undefined"), value.toString());
    }
}
