package com.pavelshapel.web.spring.boot.starter.html.element.table;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.web.spring.boot.starter.html.constant.AttributeValueId;
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
import static org.springframework.util.ReflectionUtils.makeAccessible;

@Value
@EqualsAndHashCode(callSuper = true)
public class TableHtml extends AbstractHtml {
    public static final String ID = "id";

    Class<? extends AbstractEntity> entityClass;
    Page<? extends AbstractEntity> entities;

    @Override
    public String toString() {
        List<Html> bodies = Stream.of(
                createH1TagHtml(),
                createTableTagHtml()
        ).collect(Collectors.toList());
        return createTemplateHtml(bodies).toString();
    }

    private TagHtml createH1TagHtml() {
        return createH1TagHtml(entityClass.getSimpleName());
    }

    private TagHtml createTableTagHtml() {
        Set<AttributeHtml> attributes = Stream.of(
                createAttributeHtml(BORDER, singleton(INT_1)),
                createAttributeHtml(WIDTH, singleton(WIDTH_80_PERCENT)),
                createAttributeHtml(CELLPADDING, singleton(INT_5)),
                createAlignCenterAttributeHtml()
        ).collect(Collectors.toSet());
        LinkedHashSet<Field> entityFields = getEntityFields();
        List<Html> bodies = Stream.of(
                createTHeadTagHtml(createTableHeaderTagHtml(entityFields)),
                createTBodyTagHtml(createTableBodyTagHtml()),
                createTFootTagHtml(createTableFooterTagHtml(entityFields))
        ).collect(Collectors.toList());
        return createTagHtml(
                TABLE,
                attributes,
                emptySet(),
                bodies
        );
    }

    private List<TagHtml> createTableHeaderTagHtml(Set<Field> entityFields) {
        List<TagHtml> thTagHtmlList = entityFields.stream()
                .sorted(reorderIdFieldAsFirst())
                .map(Field::getName)
                .map(this::createStringHtml)
                .map(Collections::singletonList)
                .map(this::createThTagHtml)
                .collect(Collectors.toList());
        TagHtml crudButtonTagHtml = createSimpleButtonTagHtml(CRUD_INSERT);
        thTagHtmlList.add(createThTagHtml(singletonList(crudButtonTagHtml)));
        return singletonList(createTrTagHtml(thTagHtmlList));
    }

    private List<TagHtml> createTableBodyTagHtml() {
        return entities.stream()
                .map(this::convertEntityToTdTagHtmlList)
                .map(this::addTdTagHtmlWithCrudButtons)
                .map(this::createTrTagHtml)
                .collect(Collectors.toList());
    }

    private List<TagHtml> addTdTagHtmlWithCrudButtons(List<TagHtml> tagHtmlList) {
        List<TagHtml> crudButtonTagHtmlList = Stream.of(
                createSimpleButtonTagHtml(CRUD_UPDATE),
                createSimpleButtonTagHtml(CRUD_DELETE)
        ).collect(Collectors.toList());
        tagHtmlList.add(createTdTagHtml(WIDTH_10_PERCENT, crudButtonTagHtmlList));
        return tagHtmlList;
    }

    private List<TagHtml> createTableFooterTagHtml(Set<Field> entityFields) {
        String colSpan = Integer.toString(entityFields.size() + 1);
        StringHtml colSpanStringHtml = createStringHtml(colSpan);
        AttributeHtml colSpanAttributeHtml = createAttributeHtml(COLSPAN, singleton(colSpanStringHtml));
        Set<AttributeHtml> attributeHtmlList = Stream.of(
                createBackGroundGrayAttributeHtml(),
                colSpanAttributeHtml
        ).collect(Collectors.toSet());
        TagHtml paginationTagHtml = createTdTagHtml(attributeHtmlList, createPaginationTagHtml());
        return singletonList(createTrTagHtml(singletonList(paginationTagHtml)));
    }

    private List<TagHtml> createPaginationTagHtml() {
        TagHtml firstSimpleButtonTagHtml = createSimpleButtonTagHtml(createStringHtml("https://www.google.by/"), FIRST);
        TagHtml previousSimpleButtonTagHtml = createSimpleButtonTagHtml(createStringHtml("https://www.google.by/"), PREVIOUS);
        TagHtml nextSimpleButtonTagHtml = createSimpleButtonTagHtml(createStringHtml("https://www.google.by/"), NEXT);
        TagHtml lastSimpleButtonTagHtml = createSimpleButtonTagHtml(createStringHtml("https://www.google.by/"), LAST);
        return Stream.of(
                firstSimpleButtonTagHtml,
                previousSimpleButtonTagHtml,
                nextSimpleButtonTagHtml,
                lastSimpleButtonTagHtml
        ).collect(Collectors.toList());

    }

    private List<TagHtml> convertEntityToTdTagHtmlList(AbstractEntity entity) {
        return getEntityFields().stream()
                .sorted(reorderIdFieldAsFirst())
                .map(field -> getFieldValue(entity, field))
                .map(this::createStringHtml)
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

    private <T extends Html> TagHtml createTFootTagHtml(List<T> htmlList) {
        return createTagHtml(TFOOT, htmlList);
    }

    private <T extends Html> TagHtml createTrTagHtml(List<T> htmlList) {
        return createTagHtml(TR, htmlList);
    }

    private <T extends Html> TagHtml createThTagHtml(List<T> htmlList) {
        return createTagHtml(TH, singleton(createBackGroundGrayAttributeHtml()), htmlList);
    }

    private <T extends Html> TagHtml createTdTagHtml(AttributeValueId width, List<T> htmlList) {
        Set<AttributeHtml> attributes = Stream.of(
                createAttributeHtml(WIDTH, singleton(width)),
                createAlignAttributeHtmlByType(htmlList)
        ).collect(Collectors.toSet());
        return createTdTagHtml(attributes, htmlList);
    }

    private <T extends Html> TagHtml createTdTagHtml(List<T> htmlList) {
        return createTdTagHtml(emptySet(), htmlList);
    }

    private <T extends Html> TagHtml createTdTagHtml(Set<AttributeHtml> attributes, List<T> htmlList) {
        Set<AttributeHtml> attributeHtmlSet = Stream.of(attributes, singleton(createAlignAttributeHtmlByType(htmlList)))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        return createTagHtml(TD, attributeHtmlSet, htmlList);
    }

    private Comparator<Field> reorderIdFieldAsFirst() {
        return Comparator.comparing(Field::getName, Comparator.comparing(name -> !ID.equals(name)));
    }
}