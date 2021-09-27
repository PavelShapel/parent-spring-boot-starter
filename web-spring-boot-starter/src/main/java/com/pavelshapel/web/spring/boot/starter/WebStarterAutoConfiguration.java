package com.pavelshapel.web.spring.boot.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.web.spring.boot.starter.controller.exception.handler.RestResponseEntityExceptionHandler;
import com.pavelshapel.web.spring.boot.starter.html.element.Html;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.AttributeHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.StringHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.TagHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.table.TableHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.template.TemplateHtml;
import com.pavelshapel.web.spring.boot.starter.html.factory.impl.*;
import com.pavelshapel.web.spring.boot.starter.wrapper.TypedResponseWrapperRestControllerAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Set;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Configuration
@ConditionalOnWebApplication
public class WebStarterAutoConfiguration implements WebMvcConfigurer {
    public static final String TYPE = "web";

    //inject custom objectMapper to represent date/string correctly
    @Autowired
    private ObjectMapper objectMapper;

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
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    //html
    @Bean
    public FactoryBeansCollection factoryBeansCollection() {
        return new FactoryBeansCollection();
    }

    @Bean
    public HtmlFactories htmlFactories() {
        return new HtmlFactories(factoryBeansCollection());
    }

    @Bean
    @Scope
    public AttributeHtmlFactory attributeHtmlFactory() {
        return new AttributeHtmlFactory();
    }

    @Bean
    @Scope
    public TagHtmlFactory tagHtmlFactory() {
        return new TagHtmlFactory();
    }

    @Bean
    @Scope
    public StringHtmlFactory stringHtmlFactory() {
        return new StringHtmlFactory();
    }

    @Bean
    @Scope
    public TemplateHtmlFactory templateHtmlFactory() {
        return new TemplateHtmlFactory();
    }

    @Bean
    @Scope
    public TableHtmlFactory tableHtmlFactory() {
        return new TableHtmlFactory();
    }


    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public AttributeHtml attributeHtml(String key, Set<String> values) {
        return new AttributeHtml(key, values);
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public TagHtml tagHtml(String tag, List<Html> body, Set<AttributeHtml> attributes, Set<StringHtml> modifiers) {
        return new TagHtml(tag, body, attributes, modifiers);
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public StringHtml stringHtml(String value) {
        return new StringHtml(value);
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public TemplateHtml templateHtml(List<Html> bodies) {
        return new TemplateHtml(bodies);
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public TableHtml tableHtml(Class<? extends AbstractEntity> entityClass, Page<? extends AbstractEntity> entities) {
        return new TableHtml(entityClass, entities);
    }
}