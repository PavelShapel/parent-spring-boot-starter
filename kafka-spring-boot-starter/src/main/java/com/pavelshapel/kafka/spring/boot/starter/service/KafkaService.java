package com.pavelshapel.kafka.spring.boot.starter.service;

import com.pavelshapel.web.spring.boot.starter.web.converter.AbstractDto;
import lombok.NonNull;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

public interface KafkaService {

    ListenableFuture<SendResult<String, AbstractDto>> send(@NonNull String topic, AbstractDto value);

    ListenableFuture<SendResult<String, AbstractDto>> send(@NonNull String topic, String key, AbstractDto value);

    String consumerRecordToString(ConsumerRecord<String, AbstractDto> consumerRecord, String prefix);

    String consumerRecordToString(ConsumerRecord<String, AbstractDto> consumerRecord);
}