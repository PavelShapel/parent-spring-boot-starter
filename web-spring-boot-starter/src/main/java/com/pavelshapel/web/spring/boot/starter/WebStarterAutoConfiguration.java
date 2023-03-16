package com.pavelshapel.web.spring.boot.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelshapel.web.spring.boot.starter.web.exception.handler.RestResponseEntityExceptionHandler;
import com.pavelshapel.web.spring.boot.starter.wrapper.TypedResponseWrapperRestControllerAdvice;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static com.pavelshapel.json.spring.boot.starter.JsonStarterAutoConfiguration.CUSTOM_OBJECT_MAPPER;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConditionalOnWebApplication
public class WebStarterAutoConfiguration implements WebMvcConfigurer {
    public static final String TYPE = "web";

    //inject custom objectMapper to represent date/string correctly
    @Autowired
    @Qualifier(CUSTOM_OBJECT_MAPPER)
    ObjectMapper customObjectMapper;

    @Bean
    public WebContextRefreshedListener webContextRefreshedListener() {
        return new WebContextRefreshedListener();
    }

    @Bean
    public TypedResponseWrapperRestControllerAdvice typedResponseWrapperRestControllerAdvice() {
        return new TypedResponseWrapperRestControllerAdvice();
    }

    @Bean
    public RestResponseEntityExceptionHandler restResponseEntityExceptionHandler() {
        return new RestResponseEntityExceptionHandler();
    }

    //override to represent date/string correctly
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, mappingJackson2HttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(customObjectMapper);
        return converter;
    }
}