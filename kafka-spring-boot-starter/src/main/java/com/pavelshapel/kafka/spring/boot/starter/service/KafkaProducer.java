package com.pavelshapel.kafka.spring.boot.starter.service;

import com.pavelshapel.web.spring.boot.starter.web.converter.AbstractDto;
import lombok.NonNull;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

public interface KafkaProducer<T extends AbstractDto> extends Kafka<T> {

    ListenableFuture<SendResult<String, T>> send(@NonNull String topic, T value);

    ListenableFuture<SendResult<String, T>> send(@NonNull String topic, String key, T value);
}