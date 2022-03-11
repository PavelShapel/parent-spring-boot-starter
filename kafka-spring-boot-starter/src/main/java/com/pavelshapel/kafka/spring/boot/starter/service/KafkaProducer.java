package com.pavelshapel.kafka.spring.boot.starter.service;

import com.pavelshapel.core.spring.boot.starter.model.Dto;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

public interface KafkaProducer<T extends Dto<String>> extends Kafka<T> {

    ListenableFuture<SendResult<String, T>> send(String topic, T value);

    ListenableFuture<SendResult<String, T>> send(String topic, String key, T value);
}