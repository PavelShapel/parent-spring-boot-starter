package com.pavelshapel.amqp.spring.boot.starter.rabbit;

@FunctionalInterface
public interface MessageSender {
    void send(Object pojo);
}