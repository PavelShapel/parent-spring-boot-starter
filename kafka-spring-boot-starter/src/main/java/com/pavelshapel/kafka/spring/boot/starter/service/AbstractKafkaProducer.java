package com.pavelshapel.kafka.spring.boot.starter.service;

import com.pavelshapel.web.spring.boot.starter.web.converter.AbstractDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
public abstract class AbstractKafkaProducer<T extends AbstractDto> implements KafkaProducer<T> {
    public static final String SENDING_SUCCESS = "sending.success";
    public static final String SENDING_FAIL = "sending.fail";

    @Autowired
    private KafkaTemplate<String, T> kafkaTemplate;

    @Override
    public ListenableFuture<SendResult<String, T>> send(@NonNull String topic, T value) {
        return send(topic, EMPTY, value);
    }

    @Override
    public ListenableFuture<SendResult<String, T>> send(@NonNull String topic, String key, T value) {
        String verifiedKey = isEmpty(key) ? UUID.randomUUID().toString() : key;
        ListenableFuture<SendResult<String, T>> send = kafkaTemplate.send(topic, verifiedKey, value);
        send.addCallback(
                success -> log.info(convertConsumerRecordToString(SENDING_SUCCESS, topic, verifiedKey, value)),
                fail -> log.warn(convertConsumerRecordToString(SENDING_FAIL, topic, verifiedKey, value)));
        return send;
    }
}