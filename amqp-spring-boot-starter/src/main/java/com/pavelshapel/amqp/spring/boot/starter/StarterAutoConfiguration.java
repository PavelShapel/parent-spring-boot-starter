package com.pavelshapel.amqp.spring.boot.starter;

import com.pavelshapel.amqp.spring.boot.starter.rabbit.AmqpMessageSender;
import com.pavelshapel.amqp.spring.boot.starter.rabbit.MessageSender;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import javax.annotation.PostConstruct;

@Configuration
public class StarterAutoConfiguration {
    public static final String TYPE = "amqp";
    public static final String PREFIX = "spring.pavelshapel." + TYPE;
    public static final String DEAD_PREFIX = "dead";

    private final AmqpProperties deadAmqpProperties = new AmqpProperties();
    @Autowired
    private AmqpProperties amqpProperties;

    @PostConstruct
    public void postConstruct() {
        deadAmqpProperties.setExchange(String.format("%s-%s", DEAD_PREFIX, this.amqpProperties.getExchange()));
        deadAmqpProperties.setQueue(String.format("%s-%s", DEAD_PREFIX, this.amqpProperties.getQueue()));
        deadAmqpProperties.setKey(String.format("%s.%s", DEAD_PREFIX, this.amqpProperties.getKey()));
    }

    @Bean
    public AmqpContextRefreshedListener amqpContextRefreshedListener() {
        return new AmqpContextRefreshedListener();
    }


    @Bean
    public AmqpProperties amqpProperties() {
        return new AmqpProperties();
    }


    @Bean
    public Queue queue() {
        return QueueBuilder.durable(amqpProperties().getQueue())
                .withArgument("x-dead-letter-exchange", deadAmqpProperties.getExchange())
                .withArgument("x-dead-letter-routing-key", deadAmqpProperties.getKey()).build();
    }

    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable(deadAmqpProperties.getQueue()).build();
    }


    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(amqpProperties.getExchange());
    }


    @Bean
    public TopicExchange deadExchange() {
        return new TopicExchange(deadAmqpProperties.getExchange());
    }


    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(amqpProperties.getKey());
    }

    @Bean
    public Binding deadBinding(Queue deadQueue, TopicExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(deadAmqpProperties.getKey());
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }


    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageSender amqpMessageSender() {
        return new AmqpMessageSender();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory();
    }
}