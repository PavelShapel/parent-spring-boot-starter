package com.pavelshapel.kafka.spring.boot.starter.service;

import com.pavelshapel.web.spring.boot.starter.web.converter.AbstractDto;

public interface KafkaConsumer<T extends AbstractDto> extends Kafka<T> {
    T receive(T dto);
}
