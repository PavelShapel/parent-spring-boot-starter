package com.pavelshapel.web.spring.boot.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.web.spring.boot.starter.web.exception.handler.RestResponseEntityExceptionHandler;
import com.pavelshapel.web.spring.boot.starter.html.element.Html;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.AttributeHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.StringHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.TagHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.table.TableHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.template.TemplateHtml;
import com.pavelshapel.web.spring.boot.starter.html.factory.impl.*;
import com.pavelshapel.web.spring.boot.starter.wrapper.TypedResponseWrapperRestControllerAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

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
    @Value("${spring.application.name:[spring.application.name] property not set}")
    private String applicationName;
    @Value("${spring.application.description:[spring.application.description] property not set}")
    private String applicationDescription;

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

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metadata());
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title(applicationName)
                .description(applicationDescription)
                .build();
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
    public TagHtml tagHtml(String tag, Set<AttributeHtml> attributes, Set<StringHtml> modifiers, List<Html> bodies) {
        return new TagHtml(tag, attributes, modifiers, bodies);
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