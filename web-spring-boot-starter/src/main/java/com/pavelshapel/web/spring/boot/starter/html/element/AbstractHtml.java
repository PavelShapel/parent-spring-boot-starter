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
import java.util.stream.Stream;

import static com.pavelshapel.web.spring.boot.starter.html.constant.AttributeId.*;
import static com.pavelshapel.web.spring.boot.starter.html.constant.AttributeValueId.*;
import static com.pavelshapel.web.spring.boot.starter.html.constant.TagId.BUTTON;
import static com.pavelshapel.web.spring.boot.starter.html.constant.TagId.H1;
import static java.util.Collections.*;
import static java.util.stream.Collectors.toSet;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public abstract class AbstractHtml implements Html {
    public static final String DOC_TYPE_HTML = "<!doctype html>";

    @Autowired
    HtmlFactories htmlFactories;
    @Autowired
    StreamUtils streamUtils;
    @Value("${spring.application.name}")
    String applicationName;

    Factory<TemplateHtml> templateHtmlFactory;
    Factory<TagHtml> tagHtmlFactory;
    Factory<StringHtml> stringHtmlFactory;
    Factory<AttributeHtml> attributeHtmlFactory;

    @PostConstruct
    private void postConstruct() {
        templateHtmlFactory = htmlFactories.getFactory(TemplateHtml.class);
        stringHtmlFactory = htmlFactories.getFactory(StringHtml.class);
        tagHtmlFactory = htmlFactories.getFactory(TagHtml.class);
        attributeHtmlFactory = htmlFactories.getFactory(AttributeHtml.class);

    }

    protected final TemplateHtml createTemplateHtml(List<Html> bodies) {
        return templateHtmlFactory.create(bodies);
    }

    protected final StringHtml createStringHtml(String value) {
        return stringHtmlFactory.create(value);
    }

    //tag
    protected final TagHtml createTagHtml(TagId tagId, Set<AttributeHtml> attributes) {
        return createTagHtml(tagId, attributes, emptyList());
    }

    protected final <T extends Html> TagHtml createTagHtml(TagId tagId, List<T> bodies) {
        return createTagHtml(tagId, emptySet(), bodies);
    }

    protected final <T extends Html> TagHtml createTagHtml(TagId tagId, Set<AttributeHtml> attributes, List<T> bodies) {
        return createTagHtml(tagId, attributes, emptySet(), bodies);
    }

    protected final <T extends Html> TagHtml createTagHtml(TagId tagId, Set<AttributeHtml> attributes, Set<StringHtml> modifiers, List<T> bodies) {
        return tagHtmlFactory.create(tagId, attributes, modifiers, bodies);
    }

    //tag.button
    protected final <T extends Html> TagHtml createSimpleButtonTagHtml(T href, T caption) {
        Set<AttributeHtml> attributeHtmlSet = Stream.of(
                createSimpleButtonAttributeHtml(),
                createHrefButtonAttributeHtml(href.toString())
        ).collect(toSet());
        return createButtonTagHtml(attributeHtmlSet, caption.toString());
    }

    protected final <T extends Html> TagHtml createSimpleButtonTagHtml(T caption) {
        return createButtonTagHtml(singleton(createSimpleButtonAttributeHtml()), caption.toString());
    }

    private TagHtml createButtonTagHtml(Set<AttributeHtml> typeAttributeHtmlSet, String caption) {
        StringHtml captionStringHtml = createStringHtml(caption);
        return createTagHtml(BUTTON, typeAttributeHtmlSet, singletonList(captionStringHtml));
    }


    //attribute
    protected final <T extends Html> AttributeHtml createAttributeHtml(AttributeId key, Set<T> values) {
        Set<String> stringValues = values.stream()
                .map(Html::toString)
                .collect(toSet());
        return attributeHtmlFactory.create(key.toString(), stringValues);
    }

    //attribute.align
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

    //attribute.backGround
    protected final AttributeHtml createBackGroundGrayAttributeHtml() {
        return createBackGroundColorAttributeHtml(GRAY);
    }

    protected final AttributeHtml createBackGroundColorAttributeHtml(AttributeValueId color) {
        return createAttributeHtml(BGCOLOR, singleton(color));
    }

    //attribute.button
    protected final AttributeHtml createHrefButtonAttributeHtml(String href) {
        String buttonHref = String.format(WINDOW_LOCATION_HREF.toString(), href);
        return createAttributeHtml(ONCLICK, singleton(createStringHtml(buttonHref)));
    }

    protected final AttributeHtml createSimpleButtonAttributeHtml() {
        return createButtonAttributeHtml(SIMPLE_BUTTON);
    }

    protected final AttributeHtml createSubmitButtonAttributeHtml() {
        return createButtonAttributeHtml(SUBMIT_BUTTON);
    }

    protected final AttributeHtml createResetButtonAttributeHtml() {
        return createButtonAttributeHtml(RESET_BUTTON);
    }

    protected final AttributeHtml createButtonAttributeHtml(AttributeValueId attributeValueId) {
        return createAttributeHtml(TYPE, singleton(attributeValueId));
    }


    //headers
    protected final TagHtml createH1TagHtml(String title) {
        StringHtml titleStringHtml = createStringHtml(title);
        return createTagHtml(H1, singleton(createAlignCenterAttributeHtml()), singletonList(titleStringHtml));
    }
}
