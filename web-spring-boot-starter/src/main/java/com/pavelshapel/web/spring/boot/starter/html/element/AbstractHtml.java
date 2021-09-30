package com.pavelshapel.web.spring.boot.starter.html.element;

import com.pavelshapel.core.spring.boot.starter.util.StreamUtils;
import com.pavelshapel.web.spring.boot.starter.html.constant.AttributeId;
import com.pavelshapel.web.spring.boot.starter.html.constant.AttributeValueId;
import com.pavelshapel.web.spring.boot.starter.html.constant.TagId;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.AttributeHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.StringHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.TagHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.template.TemplateHtml;
import com.pavelshapel.web.spring.boot.starter.html.factory.Factory;
import com.pavelshapel.web.spring.boot.starter.html.factory.impl.HtmlFactories;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.pavelshapel.web.spring.boot.starter.html.constant.AttributeId.*;
import static com.pavelshapel.web.spring.boot.starter.html.constant.AttributeValueId.*;
import static com.pavelshapel.web.spring.boot.starter.html.constant.TagId.H1;
import static com.pavelshapel.web.spring.boot.starter.html.constant.TagId.BUTTON;
import static java.util.Collections.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractHtml implements Html {
    public static final String DOC_TYPE_HTML = "<!doctype html>";

    @Autowired
    HtmlFactories htmlFactories;
    @Autowired
    StreamUtils streamUtils;
    @Value("${spring.application.name}")
    @Getter
    String applicationName;

    Factory<TemplateHtml> templateHtmlFactory;
    Factory<TagHtml> tagHtmlFactory;
    Factory<StringHtml> stringHtmlFactory;
    Factory<AttributeHtml> attributeHtmlFactory;

    @PostConstruct
    private void postConstruct() {
        tagHtmlFactory = htmlFactories.getFactory(TagHtml.class);
        stringHtmlFactory = htmlFactories.getFactory(StringHtml.class);
        attributeHtmlFactory = htmlFactories.getFactory(AttributeHtml.class);
        templateHtmlFactory = htmlFactories.getFactory(TemplateHtml.class);
    }

    protected final TemplateHtml createTemplateHtml(List<Html> bodies) {
        return templateHtmlFactory.create(bodies);
    }

    protected final StringHtml createStringHtml(String value) {
        return stringHtmlFactory.create(value);
    }

    protected final <T> AttributeHtml createAttributeHtml(AttributeId key, Set<T> values) {
        Set<String> stringValues = values.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
        return attributeHtmlFactory.create(key.toString(), stringValues);
    }

    protected final <T extends Html> TagHtml createTagHtml(TagId tag,
                                                           List<T> bodies) {
        return createTagHtml(tag, emptySet(), bodies);
    }

    protected final <T extends Html> TagHtml createTagHtml(TagId tag,
                                                           Set<AttributeHtml> attributes,
                                                           List<T> bodies) {
        return createTagHtml(tag, attributes, emptySet(), bodies);
    }

    protected final <T extends Html> TagHtml createTagHtml(TagId tag,
                                                           Set<AttributeHtml> attributes,
                                                           Set<StringHtml> modifiers,
                                                           List<T> bodies) {
        return tagHtmlFactory.create(tag.toString(), attributes, modifiers, bodies);
    }

    protected final <T extends Html> AttributeHtml createAlignAttributeHtmlByType(List<T> htmlList) {
        return htmlList.stream().collect(streamUtils.toSingleton())
                .map(Html::toString)
                .map(this::createAlignAttributeHtmlByType)
                .orElseGet(this::createAlignCenterAttributeHtml);
    }

    protected final AttributeHtml createAlignAttributeHtmlByType(String value) {
        if (NumberUtils.isParsable(value)) {
            return createAlignRightAttributeHtml();
        }
        try {
            LocalDate.parse(value);
            return createAlignCenterAttributeHtml();
        } catch (Exception e) {
            return createAlignLeftAttributeHtml();
        }
    }

    protected final AttributeHtml createAlignCenterAttributeHtml() {
        return createAttributeHtml(ALIGN, singleton(CENTER));
    }

    protected final AttributeHtml createAlignLeftAttributeHtml() {
        return createAttributeHtml(ALIGN, singleton(LEFT));
    }

    protected final AttributeHtml createAlignRightAttributeHtml() {
        return createAttributeHtml(ALIGN, singleton(RIGHT));
    }

    protected final AttributeHtml createBackGroundGrayAttributeHtml() {
        return createBackGroundColorAttributeHtml(GRAY);
    }

    protected final AttributeHtml createBackGroundColorAttributeHtml(AttributeValueId color) {
        return createAttributeHtml(BGCOLOR, singleton(color));
    }

    protected final TagHtml createSimpleButtonTagHtml(String caption) {
        return createButtonTagHtml(singleton(createSimpleButtonAttributeHtml()), caption);
    }

    protected final AttributeHtml createSimpleButtonAttributeHtml() {
        return createAttributeHtml(TYPE, singleton(SIMPLE_BUTTON));
    }

    protected final AttributeHtml createSubmitButtonAttributeHtml() {
        return createAttributeHtml(TYPE, singleton(SUBMIT_BUTTON));
    }

    protected final AttributeHtml createResetButtonAttributeHtml() {
        return createAttributeHtml(TYPE, singleton(RESET_BUTTON));
    }

    private TagHtml createButtonTagHtml(Set<AttributeHtml> typeAttributeHtmlSet, String caption) {
        StringHtml captionStringHtml = createStringHtml(caption);
        return createTagHtml(
                BUTTON,
                typeAttributeHtmlSet,
                singletonList(captionStringHtml));
    }

    protected final TagHtml createH1TagHtml(String title) {
        StringHtml titleStringHtml = createStringHtml(title);
        return createTagHtml(
                H1,
                singleton(createAlignCenterAttributeHtml()),
                singletonList(titleStringHtml)
        );
    }
}
