package com.pavelshapel.web.spring.boot.starter.html.factory.impl;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.json.spring.boot.starter.JsonStarterAutoConfiguration;
import com.pavelshapel.web.spring.boot.starter.WebStarterAutoConfiguration;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.AttributeHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.StringHtml;
import com.pavelshapel.web.spring.boot.starter.wrapper.provider.ThreeStringProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        WebStarterAutoConfiguration.class,
        JsonStarterAutoConfiguration.class,
        CoreStarterAutoConfiguration.class
})
class HtmlFactoriesTest {

    @Autowired
    private HtmlFactories htmlFactories;

    @Test
    void initialize() {
        assertThat(htmlFactories).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(ThreeStringProvider.class)
    void create_AttributeHtmlFactory_ShouldReturnAttributeHtml(String key, String value1, String value2) {
        HashSet<String> set = Stream.of(value1, value2).collect(Collectors.toCollection(LinkedHashSet::new));
        String actualAttributeHtml = htmlFactories.getFactory(AttributeHtml.class)
                .create(key, set)
                .toString();
        String expectedAttributeHtml = String.format("%s=\"%s %s\"", key, value1, value2);

        assertThat(actualAttributeHtml).isEqualTo(expectedAttributeHtml);
    }

    @ParameterizedTest
    @ArgumentsSource(ThreeStringProvider.class)
    void create_StringHtmlFactory_ShouldReturnStringHtml(String value) {
        String actualStringHtml = htmlFactories.getFactory(StringHtml.class)
                .create(value)
                .toString();

        assertThat(actualStringHtml).isEqualTo(value);
    }
}