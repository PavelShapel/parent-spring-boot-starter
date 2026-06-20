package com.pavelshapel.starter.boot.spring.ordered;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

public abstract class OrderedComponentsProcessor<
        PAYLOAD, RESULT, COMPONENT extends OrderedComponent<PAYLOAD, RESULT>>
    implements Function<PAYLOAD, List<RESULT>> {
  private final List<COMPONENT> components;

  private List<COMPONENT> orderedComponents;

  protected OrderedComponentsProcessor(List<COMPONENT> components) {
    this.components = components;
  }

  @PostConstruct
  private void init() {
    orderedComponents =
        components.stream()
            .filter(this::isComponentPresentInProcessingOrder)
            .sorted(comparing(this::getProcessingOrder))
            .toList();
  }

  @Override
  public final List<RESULT> apply(PAYLOAD payload) {
    return getApplicableComponentsStream(payload)
        .map(component -> component.apply(payload))
        .toList();
  }

  public final COMPONENT getSingle(PAYLOAD payload) {
    return getSingle(payload, _ -> true);
  }

  public final COMPONENT getSingle(PAYLOAD payload, Predicate<COMPONENT> predicate) {
    return getApplicableComponentsStream(payload).filter(predicate).collect(toSingle());
  }

  protected List<Class<? extends COMPONENT>> getClassesInProcessingOrder() {
    return components.stream()
        .map(COMPONENT::getClass)
        .map(componentClass -> (Class<? extends COMPONENT>) componentClass)
        .collect(toList());
  }

  private Stream<COMPONENT> getApplicableComponentsStream(PAYLOAD payload) {
    return orderedComponents.stream().filter(component -> component.isApplicable(payload));
  }

  private boolean isComponentPresentInProcessingOrder(COMPONENT component) {
    List<Class<? extends COMPONENT>> classesInProcessingOrder = getClassesInProcessingOrder();
    return !isEmpty(classesInProcessingOrder)
        && classesInProcessingOrder.contains(component.getClass());
  }

  private int getProcessingOrder(COMPONENT component) {
    return getClassesInProcessingOrder().indexOf(component.getClass());
  }

  public static <T> Collector<T, ?, T> toSingle() {
    return collectingAndThen(
        toList(),
        list -> {
          if (list.size() != 1) {
            throw new IllegalArgumentException("Expected exactly 1 element, got " + list.size());
          }
          return list.getFirst();
        });
  }
}
