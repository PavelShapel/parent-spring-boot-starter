package com.pavelshapel.web.spring.boot.starter.html.element;

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
import static java.util.Collections.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractHtml implements Html {
    public static final String DOC_TYPE_HTML = "<!doctype html>";

    @Autowired
    HtmlFactories htmlFactories;
    @Value("${spring.application.name}")
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

    protected final <T extends Html> AttributeHtml createAlignAttributeHtmlByType(List<T> htmlList) {
        String value = htmlList.stream()
                .map(Html::toString)
                .collect(Collectors.joining());
        return createAlignAttributeHtmlByType(value);
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
        return getAttributeHtmlFactory().create(ALIGN.toString(), singleton(CENTER.toString()));
    }

    protected final AttributeHtml createAlignLeftAttributeHtml() {
        return getAttributeHtmlFactory().create(ALIGN.toString(), singleton(LEFT.toString()));
    }

    protected final AttributeHtml createAlignRightAttributeHtml() {
        return getAttributeHtmlFactory().create(ALIGN.toString(), singleton(RIGHT.toString()));
    }

    protected final AttributeHtml createBackGroundGrayAttributeHtml() {
        return createBackGroundColorAttributeHtml(GRAY.toString());
    }

    protected final AttributeHtml createBackGroundColorAttributeHtml(String color) {
        return getAttributeHtmlFactory().create(BGCOLOR.toString(), singleton(color));
    }

    protected final TagHtml createSimpleButtonTagHtml(String caption) {
        return createButtonTagHtml(singleton(createSimpleButtonAttributeHtml()), caption);
    }

    protected final AttributeHtml createSimpleButtonAttributeHtml() {
        return getAttributeHtmlFactory().create(TYPE.toString(), singleton(BUTTON.toString()));
    }

    protected final AttributeHtml createSubmitButtonAttributeHtml() {
        return getAttributeHtmlFactory().create(TYPE.toString(), singleton(SUBMIT.toString()));
    }

    protected final AttributeHtml createResetButtonAttributeHtml() {
        return getAttributeHtmlFactory().create(TYPE.toString(), singleton(RESET.toString()));
    }

    private TagHtml createButtonTagHtml(Set<AttributeHtml> typeAttributeHtmlSet, String caption) {
        StringHtml captionStringHtml = getStringHtmlFactory().create(caption);
        return getTagHtmlFactory().create(
                BUTTON.toString(),
                typeAttributeHtmlSet,
                emptySet(),
                singletonList(captionStringHtml));
    }

    protected final TagHtml createH1TagHtml(String title) {
        StringHtml titleStringHtml = getStringHtmlFactory().create(title);
        return getTagHtmlFactory().create(
                H1.toString(),
                singleton(createAlignCenterAttributeHtml()),
                emptySet(),
                singletonList(titleStringHtml)
        );
    }
}
