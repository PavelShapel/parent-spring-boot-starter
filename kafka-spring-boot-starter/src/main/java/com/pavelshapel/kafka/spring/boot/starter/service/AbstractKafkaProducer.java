package com.pavelshapel.kafka.spring.boot.starter.service;

import com.pavelshapel.core.spring.boot.starter.model.Dto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
public abstract class AbstractKafkaProducer<T extends Dto<String>> implements KafkaProducer<T> {
    public static final String SENDING_SUCCESS = "sending.success";
    public static final String SENDING_FAIL = "sending.fail";

    @Autowired
    private KafkaTemplate<String, T> kafkaTemplate;

    @Override
    public ListenableFuture<SendResult<String, T>> send(String topic, T value) {
        return send(topic, null, value);
    }

    @Override
    public ListenableFuture<SendResult<String, T>> send(String topic, String key, T value) {
        ListenableFuture<SendResult<String, T>> send = kafkaTemplate.send(topic, key, value);
        send.addCallback(
                success -> log.info(convertConsumerRecordToString(SENDING_SUCCESS, topic, key, value)),
                fail -> log.warn(convertConsumerRecordToString(SENDING_FAIL, topic, key, value)));
        kafkaTemplate.flush();
        return send;
    }
}