package com.pavelshapel.kafka.spring.boot.starter;

import com.pavelshapel.kafka.spring.boot.starter.aop.KafkaSenderAnnotationBeanPostProcessor;
import com.pavelshapel.kafka.spring.boot.starter.properties.KafkaProperties;
import com.pavelshapel.kafka.spring.boot.starter.service.GenericKafkaService;
import com.pavelshapel.kafka.spring.boot.starter.service.KafkaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaStarterAutoConfiguration {
    public static final String TYPE = "kafka";

    @Bean
    public KafkaContextRefreshedListener kafkaContextRefreshedListener() {
        return new KafkaContextRefreshedListener();
    }

    @Bean
    @ConditionalOnProperty(prefix = TYPE, name = "server")
    public KafkaService kafkaService() {
        return new GenericKafkaService();
    }

    @Bean
    @ConditionalOnProperty(prefix = TYPE, name = "server")
    public KafkaSenderAnnotationBeanPostProcessor kafkaSenderAnnotationBeanPostProcessor() {
        return new KafkaSenderAnnotationBeanPostProcessor();
    }
}