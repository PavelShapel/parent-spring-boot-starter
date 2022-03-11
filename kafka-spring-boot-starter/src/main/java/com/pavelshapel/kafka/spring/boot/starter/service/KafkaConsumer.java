package com.pavelshapel.kafka.spring.boot.starter.service;

import com.pavelshapel.core.spring.boot.starter.model.Dto;

public interface KafkaConsumer<T extends Dto<String>> extends Kafka<T> {
    T receive(T dto);
}
