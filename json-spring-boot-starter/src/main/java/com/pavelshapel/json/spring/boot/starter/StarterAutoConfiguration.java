package com.pavelshapel.json.spring.boot.starter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pavelshapel.json.spring.boot.starter.converter.jackson.JacksonJsonConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.text.SimpleDateFormat;

@Configuration
public class StarterAutoConfiguration {
    public static final String TYPE = "json";

    @Bean
    public JsonContextRefreshedListener jsonContextRefreshedListener() {
        return new JsonContextRefreshedListener();
    }

    @Bean
    @ConditionalOnClass(ObjectMapper.class)
    public JacksonJsonConverter jacksonJsonConverter(ObjectMapper objectMapper) {
        return new JacksonJsonConverter(objectMapper);
    }

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

        return mapper;
    }
}