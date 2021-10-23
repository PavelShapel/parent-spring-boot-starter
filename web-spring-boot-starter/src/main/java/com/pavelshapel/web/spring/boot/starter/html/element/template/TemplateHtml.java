package com.pavelshapel.web.spring.boot.starter.html.element.template;


import com.pavelshapel.web.spring.boot.starter.html.element.AbstractHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.Html;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.AttributeHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.StringHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.TagHtml;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

import static com.pavelshapel.web.spring.boot.starter.html.constant.AttributeId.CHARSET;
import static com.pavelshapel.web.spring.boot.starter.html.constant.AttributeValueId.UTF_8;
import static com.pavelshapel.web.spring.boot.starter.html.constant.TagId.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

@Value
@EqualsAndHashCode(callSuper = true)
public class TemplateHtml extends AbstractHtml {
    List<Html> bodies;

    @Override
    public String toString() {
        StringHtml headTitleStringHtml = createStringHtml(getApplicationName());
        TagHtml titleTagHtml = createTagHtml(TITLE, singletonList(headTitleStringHtml));
        AttributeHtml charsetAttributeHtml = createAttributeHtml(CHARSET, singleton(UTF_8));
        TagHtml metaTagHtml = createTagHtml(META, singleton(charsetAttributeHtml));
        TagHtml headTagHtml = createTagHtml(HEAD, asList(metaTagHtml, titleTagHtml));
        TagHtml bodyTagHtml = createTagHtml(BODY, bodies);
        TagHtml htmlTagHtml = createTagHtml(HTML, asList(headTagHtml, bodyTagHtml));
        return String.format("%s%s", DOC_TYPE_HTML, htmlTagHtml.toString());
    }
}