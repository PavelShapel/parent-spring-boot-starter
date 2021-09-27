package com.pavelshapel.web.spring.boot.starter.html.element.simple;

import com.pavelshapel.web.spring.boot.starter.html.element.Html;
import lombok.Value;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.SPACE;

@Value
public class TagHtml implements Html {
    String tag;
    List<Html> bodies;
    Set<AttributeHtml> attributes;
    Set<StringHtml> modifiers;

    @Override
    public String toString() {
        String joinedAttributes = attributes.stream()
                .map(AttributeHtml::toString)
                .collect(Collectors.joining(SPACE));
        String joinedModifiers = modifiers.stream()
                .map(StringHtml::toString)
                .collect(Collectors.joining(SPACE));
        String joinedBody = bodies.stream()
                .map(Html::toString)
                .collect(Collectors.joining());
        return String.format("<%1$s %2$s %3$s>%4$s</%1$s>", tag, joinedAttributes, joinedModifiers, joinedBody);
    }
}