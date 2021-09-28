package com.pavelshapel.web.spring.boot.starter.html.element.template;


import com.pavelshapel.web.spring.boot.starter.html.element.AbstractHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.Html;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.AttributeHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.StringHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.TagHtml;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pavelshapel.web.spring.boot.starter.html.constant.AttributeId.CHARSET;
import static com.pavelshapel.web.spring.boot.starter.html.constant.AttributeValueId.UTF_8;
import static com.pavelshapel.web.spring.boot.starter.html.constant.TagId.*;
import static java.util.Collections.*;

@Value
@EqualsAndHashCode(callSuper = true)
public class TemplateHtml extends AbstractHtml {
    List<Html> bodies;

    @Override
    public String toString() {
        StringHtml headTitleStringHtml = getStringHtmlFactory().create(getApplicationName());
        TagHtml titleTagHtml = getTagHtmlFactory().create(TITLE.toString(), emptySet(), emptySet(), singletonList(headTitleStringHtml));
        AttributeHtml charsetAttributeHtml = getAttributeHtmlFactory().create(CHARSET.toString(), singleton(UTF_8.toString()));
        TagHtml metaTagHtml = getTagHtmlFactory().create(META.toString(), singleton(charsetAttributeHtml), emptySet(), emptyList());
        TagHtml headTagHtml = getTagHtmlFactory().create(HEAD.toString(), emptySet(), emptySet(), Stream.of(metaTagHtml, titleTagHtml).collect(Collectors.toList()));
        TagHtml bodyTagHtml = getTagHtmlFactory().create(BODY.toString(), emptySet(), emptySet(), bodies);
        TagHtml htmlTagHtml = getTagHtmlFactory().create(HTML.toString(), emptySet(), emptySet(), Stream.of(headTagHtml, bodyTagHtml).collect(Collectors.toList()));
        return String.format("%s%s", DOC_TYPE_HTML, htmlTagHtml.toString());
    }
}