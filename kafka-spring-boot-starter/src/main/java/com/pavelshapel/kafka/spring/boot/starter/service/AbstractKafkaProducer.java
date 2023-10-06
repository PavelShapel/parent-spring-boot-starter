//package com.pavelshapel.kafka.spring.boot.starter.service;
//
//import com.pavelshapel.core.spring.boot.starter.api.model.Dto;
//import lombok.AccessLevel;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.java.Log;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.util.concurrent.ListenableFuture;
//
//import static java.util.logging.Level.INFO;
//import static java.util.logging.Level.WARNING;
//
//@Log
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public abstract class AbstractKafkaProducer<T extends Dto<String>> implements KafkaProducer<T> {
//    public static final String SENDING_SUCCESS = "sending.success";
//    public static final String SENDING_FAIL = "sending.fail";
//
//    @Autowired
//    KafkaTemplate<String, T> kafkaTemplate;
//
//    @Override
//    public ListenableFuture<SendResult<String, T>> send(String topic, T value) {
//        return send(topic, null, value);
//    }
//
//    @Override
//    public ListenableFuture<SendResult<String, T>> send(String topic, String key, T value) {
//        ListenableFuture<SendResult<String, T>> send = kafkaTemplate.send(topic, key, value);
//        send.addCallback(
//                success -> log.log(INFO, convertConsumerRecordToString(SENDING_SUCCESS, topic, key, value)),
//                fail -> log.log(WARNING, convertConsumerRecordToString(SENDING_FAIL, topic, key, value)));
//        kafkaTemplate.flush();
//        return send;
//    }
//}