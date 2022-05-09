package com.pavelshapel.kafka.spring.boot.starter.config;

import com.pavelshapel.kafka.spring.boot.starter.properties.KafkaProperties;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.kafka.core.KafkaAdmin;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.uncapitalize;

@EnableConfigurationProperties(KafkaProperties.class)
public abstract class AbstractKafkaTopicConfiguration {
    @Autowired
    private GenericApplicationContext context;
    @Autowired
    private KafkaProperties kafkaProperties;

    @PostConstruct
    private void postConstruct() {
        getTopics().forEach(this::registerTopicBean);
    }

    private void registerTopicBean(NewTopic topic) {
        String className = NewTopic.class.getSimpleName();
        context.registerBean(
                String.format("%s%s", uncapitalize(topic.name()), className),
                NewTopic.class,
                () -> topic);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServer());
        return new KafkaAdmin(configs);
    }

    protected abstract List<NewTopic> getTopics();
}