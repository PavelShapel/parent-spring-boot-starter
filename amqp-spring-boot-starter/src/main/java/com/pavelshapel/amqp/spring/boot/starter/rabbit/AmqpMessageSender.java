package com.pavelshapel.amqp.spring.boot.starter.rabbit;

import com.pavelshapel.amqp.spring.boot.starter.AmqpProperties;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class AmqpMessageSender implements MessageSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private AmqpProperties amqpProperties;

    @Override
    public void send(Object pojo) {
        rabbitTemplate.convertAndSend(
                amqpProperties.getExchange(),
                amqpProperties.getKey(),
                pojo);
    }
}
