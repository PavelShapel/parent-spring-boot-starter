package com.pavelshapel.web.spring.boot.starter.html.element.table;


import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.web.spring.boot.starter.html.element.AbstractHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.AttributeHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.StringHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.TagHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.template.TemplateHtml;
import com.pavelshapel.web.spring.boot.starter.html.factory.Factory;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.util.ReflectionUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;

@Value
@EqualsAndHashCode(callSuper = true)
public class TableHtml extends AbstractHtml {
    Class<? extends AbstractEntity> entityClass;
    Page<? extends AbstractEntity> entities;

    @Override
    public String toString() {
        Factory<TemplateHtml> templateHtmlFactory = getHtmlFactories().getFactory(TemplateHtml.class);
        Factory<TagHtml> tagHtmlFactory = getHtmlFactories().getFactory(TagHtml.class);
        Factory<StringHtml> stringHtmlFactory = getHtmlFactories().getFactory(StringHtml.class);
        Factory<AttributeHtml> attributeHtmlFactory = getHtmlFactories().getFactory(AttributeHtml.class);
        StringHtml titleStringHtml = stringHtmlFactory.create(entityClass.getSimpleName());
        TagHtml h1TagHtml = tagHtmlFactory.create(H1, singletonList(titleStringHtml), emptySet(), emptySet());

        ReflectionUtils.doWithFields(entityClass,
                field -> {

                }
        );


        return templateHtmlFactory.create(Stream.of(h1TagHtml).collect(Collectors.toList())).toString();
    }
}