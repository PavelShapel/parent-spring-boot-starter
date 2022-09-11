package com.pavelshapel.json.spring.boot.starter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pavelshapel.core.spring.boot.starter.api.util.ExceptionUtils;
import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import com.pavelshapel.json.spring.boot.starter.converter.jackson.JacksonJsonConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class JsonStarterAutoConfiguration {
    public static final String TYPE = "json";
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String CUSTOM_OBJECT_MAPPER = "customObjectMapper";

    @Bean
    public JsonContextRefreshedListener jsonContextRefreshedListener() {
        return new JsonContextRefreshedListener();
    }

    @Bean
    @ConditionalOnClass(ObjectMapper.class)
    public JsonConverter jacksonJsonConverter(@Qualifier(CUSTOM_OBJECT_MAPPER) ObjectMapper customObjectMapper, ExceptionUtils exceptionUtils) {
        return new JacksonJsonConverter(customObjectMapper, exceptionUtils);
    }

    @Bean
    public ObjectMapper customObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDateFormat(new SimpleDateFormat(DATE_PATTERN));
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}