package com.pavelshapel.kafka.spring.boot.starter.service;

import com.pavelshapel.web.spring.boot.starter.web.converter.AbstractDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface KafkaConsumer<T extends AbstractDto> extends Kafka<T> {
    T receive(ConsumerRecord<String, T> consumerRecord);
}
