package com.pavelshapel.web.spring.boot.starter.html.element.template;


import com.pavelshapel.web.spring.boot.starter.html.element.AbstractHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.Html;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.AttributeHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.StringHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.TagHtml;
import com.pavelshapel.web.spring.boot.starter.html.factory.Factory;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.*;

@Value
@EqualsAndHashCode(callSuper = true)
public class TemplateHtml extends AbstractHtml {
    List<Html> bodies;

    @Override
    public String toString() {
        Factory<TagHtml> tagHtmlFactory = getHtmlFactories().getFactory(TagHtml.class);
        Factory<StringHtml> stringHtmlFactory = getHtmlFactories().getFactory(StringHtml.class);
        Factory<AttributeHtml> attributeHtmlFactory = getHtmlFactories().getFactory(AttributeHtml.class);
        StringHtml headTitleStringHtml = stringHtmlFactory.create(getApplicationName());
        TagHtml titleTagHtml = tagHtmlFactory.create(TITLE, singletonList(headTitleStringHtml), emptySet(), emptySet());
        AttributeHtml metaAttributeHtml = attributeHtmlFactory.create(CHARSET, singleton(StandardCharsets.UTF_8.name()));
        TagHtml metaTagHtml = tagHtmlFactory.create(META, emptyList(), singleton(metaAttributeHtml), emptySet());
        TagHtml headTagHtml = tagHtmlFactory.create(HEAD, Stream.of(metaTagHtml, titleTagHtml).collect(Collectors.toList()), emptySet(), emptySet());
        TagHtml bodyTagHtml = tagHtmlFactory.create(BODY, bodies, emptySet(), emptySet());
        TagHtml htmlTagHtml = tagHtmlFactory.create(HTML, Stream.of(headTagHtml, bodyTagHtml).collect(Collectors.toList()), emptySet(), emptySet());
        return String.format("%s%s", DOC_TYPE_HTML, htmlTagHtml.toString());
    }
}