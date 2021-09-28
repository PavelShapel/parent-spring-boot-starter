package com.pavelshapel.web.spring.boot.starter.html.element.table;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.web.spring.boot.starter.html.constant.TagId;
import com.pavelshapel.web.spring.boot.starter.html.element.AbstractHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.Html;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.AttributeHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.StringHtml;
import com.pavelshapel.web.spring.boot.starter.html.element.simple.TagHtml;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pavelshapel.web.spring.boot.starter.html.constant.AttributeId.*;
import static com.pavelshapel.web.spring.boot.starter.html.constant.AttributeValueId.*;
import static com.pavelshapel.web.spring.boot.starter.html.constant.TagId.*;
import static java.util.Collections.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.util.ReflectionUtils.makeAccessible;

@Value
@EqualsAndHashCode(callSuper = true)
public class TableHtml extends AbstractHtml {
    public static final String ID = "id";

    Class<? extends AbstractEntity> entityClass;
    Page<? extends AbstractEntity> entities;

    @Override
    public String toString() {
        return getTemplateHtmlFactory().create(
                Stream.of(
                        createH1TagHtml(),
                        createTableTagHtml())
                        .collect(Collectors.toList()))
                .toString();
    }

    private TagHtml createH1TagHtml() {
        return createH1TagHtml(entityClass.getSimpleName());
    }

    private TagHtml createTableTagHtml() {
        AttributeHtml borderAttributeHtml = getAttributeHtmlFactory().create(BORDER.toString(), singleton(INT_1.toString()));
        AttributeHtml widthAttributeHtml = getAttributeHtmlFactory().create(WIDTH.toString(), singleton(WIDTH_80_PERCENT.toString()));
        AttributeHtml cellPaddingAttributeHtml = getAttributeHtmlFactory().create(CELLPADDING.toString(), singleton(INT_5.toString()));
        TagHtml tableTHeadTagHtml = createTHeadTagHtml(singletonList(createTableHeaderTagHtml(getEntityFields())));
        TagHtml tableTBodyTagHtml = createTBodyTagHtml(createTableBodyTagHtml());
        return getTagHtmlFactory().create(
                TABLE.toString(),
                Stream.of(
                        borderAttributeHtml,
                        widthAttributeHtml,
                        cellPaddingAttributeHtml,
                        createAlignCenterAttributeHtml()
                ).collect(Collectors.toSet()),
                emptySet(),
                Stream.of(
                        tableTHeadTagHtml,
                        tableTBodyTagHtml
                ).collect(Collectors.toList())
        );
    }

    private TagHtml createTableHeaderTagHtml(Set<Field> entityFields) {
        StringHtml emptyStringHtml = getStringHtmlFactory().create(EMPTY);
        List<TagHtml> thTagHtmlList = entityFields.stream()
                .sorted(reorderIdFieldAsFirst())
                .map(Field::getName)
                .map(name -> getStringHtmlFactory().create(name))
                .map(Collections::singletonList)
                .map(this::createThTagHtml)
                .collect(Collectors.toList());
        thTagHtmlList.add(thTagHtmlList.size(), createThTagHtml(singletonList(emptyStringHtml)));
        return createTrTagHtml(thTagHtmlList);
    }

    private List<TagHtml> createTableBodyTagHtml() {
        return entities.stream()
                .map(this::convertEntityToTdTagHtmlList)
                .peek(tagHtmlList ->
                        tagHtmlList.add(tagHtmlList.size(), createTdTagHtml(Stream.of(createSimpleButtonTagHtml(CRUD_UPDATE.toString()), createSimpleButtonTagHtml(CRUD_DELETE.toString())).collect(Collectors.toList()))))
                .map(this::createTrTagHtml)
                .collect(Collectors.toList());
    }

    private List<TagHtml> convertEntityToTdTagHtmlList(AbstractEntity entity) {
        return getEntityFields().stream()
                .sorted(reorderIdFieldAsFirst())
                .map(field -> getStringHtmlFactory().create(getFieldValue(entity, field)))
                .map(Collections::singletonList)
                .map(this::createTdTagHtml)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private String getFieldValue(Object entity, Field field) {
        makeAccessible(field);
        return field.get(entity).toString();
    }

    private LinkedHashSet<Field> getEntityFields() {
        LinkedHashSet<Field> fields = new LinkedHashSet<>();
        ReflectionUtils.doWithFields(
                entityClass,
                fields::add,
                field -> !Modifier.isStatic(field.getModifiers()) &&
                        !Modifier.isFinal(field.getModifiers()) &&
                        !field.isAnnotationPresent(JsonIgnore.class));
        return fields;
    }

    private <T extends Html> TagHtml createTHeadTagHtml(List<T> htmlList) {
        return createTagHtml(THEAD, htmlList);
    }

    private <T extends Html> TagHtml createTBodyTagHtml(List<T> htmlList) {
        return createTagHtml(TBODY, htmlList);
    }

    private <T extends Html> TagHtml createTrTagHtml(List<T> htmlList) {
        return createTagHtml(TR, htmlList);
    }

    private <T extends Html> TagHtml createTagHtml(TagId tagId, List<T> htmlList) {
        return getTagHtmlFactory().create(
                tagId.toString(),
                emptySet(),
                emptySet(),
                htmlList);
    }

    private Comparator<Field> reorderIdFieldAsFirst() {
        return Comparator.comparing(Field::getName, Comparator.comparing(name -> !ID.equals(name)));
    }

    private <T extends Html> TagHtml createThTagHtml(List<T> htmlList) {
        return createTableCellTagHtml(TH, singleton(createBackGroundGrayAttributeHtml()), htmlList);
    }

    private <T extends Html> TagHtml createTdTagHtml(List<T> htmlList) {
        return createTableCellTagHtml(TD, singleton(createAlignAttributeHtmlByType(htmlList)), htmlList);
    }

    private <T extends Html> TagHtml createTableCellTagHtml(TagId tagId, Set<AttributeHtml> attributeHtmlSet, List<T> htmlList) {
        return getTagHtmlFactory().create(
                tagId.toString(),
                attributeHtmlSet,
                emptySet(),
                htmlList);
    }
}