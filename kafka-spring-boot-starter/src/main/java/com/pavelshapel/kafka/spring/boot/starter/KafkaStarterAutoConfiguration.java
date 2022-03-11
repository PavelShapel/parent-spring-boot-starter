package com.pavelshapel.kafka.spring.boot.starter;

import com.pavelshapel.kafka.spring.boot.starter.bpp.KafkaSenderAnnotationBeanPostProcessor;
import com.pavelshapel.kafka.spring.boot.starter.properties.KafkaProperties;
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
    public KafkaSenderAnnotationBeanPostProcessor kafkaSenderAnnotationBeanPostProcessor() {
        return new KafkaSenderAnnotationBeanPostProcessor();
    }
}