package com.pavelshapel.kafka.spring.boot.starter.service;

import com.pavelshapel.web.spring.boot.starter.web.converter.AbstractDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
public class GenericKafkaService implements KafkaService {
    public static final String SENDING_SUCCESS = "sending.success";
    public static final String SENDING_FAIL = "sending.fail";

    @Autowired
    private KafkaTemplate<String, AbstractDto> kafkaTemplate;

    @Override
    public ListenableFuture<SendResult<String, AbstractDto>> send(@NonNull String topic, AbstractDto value) {
        return send(topic, EMPTY, value);
    }

    @Override
    public ListenableFuture<SendResult<String, AbstractDto>> send(@NonNull String topic, String key, AbstractDto value) {
        String verifiedKey = isEmpty(key) ? RandomStringUtils.randomAlphanumeric(1, Byte.MAX_VALUE) : key;
        ListenableFuture<SendResult<String, AbstractDto>> send = kafkaTemplate.send(topic, verifiedKey, value);
        send.addCallback(
                success -> log.info(consumerRecordToString(SENDING_SUCCESS, topic, verifiedKey, value)),
                fail -> log.warn(consumerRecordToString(SENDING_FAIL, topic, verifiedKey, value)));
        return send;
    }

    @Override
    public String consumerRecordToString(ConsumerRecord<String, AbstractDto> consumerRecord) {
        return consumerRecordToString(consumerRecord, EMPTY);
    }

    @Override
    public String consumerRecordToString(ConsumerRecord<String, AbstractDto> consumerRecord, String prefix) {
        return consumerRecordToString(prefix, consumerRecord.topic(), consumerRecord.key(), consumerRecord.value());
    }

    private String consumerRecordToString(String prefix, String topic, String key, AbstractDto value) {
        return String.format("%s [topic [%s], key [%s], value [%s]]", prefix, topic, key, value);
    }
}