package com.pavelshapel.ordered.spring.boot.starter;

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

public abstract class OrderedComponentsProcessor<P, R, C extends OrderedComponent<P, R>>
    implements Function<P, List<R>> {
  private final List<C> components;

  private List<C> orderedComponents;

  protected OrderedComponentsProcessor(List<C> components) {
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
  public final List<R> apply(P payload) {
    return getApplicableComponentsStream(payload)
        .map(component -> component.apply(payload))
        .toList();
  }

  public final C getSingle(P payload) {
    return getSingle(payload, _ -> true);
  }

  public final C getSingle(P payload, Predicate<C> predicate) {
    return getApplicableComponentsStream(payload).filter(predicate).collect(toSingle());
  }

  protected List<Class<? extends C>> getClassesInProcessingOrder() {
    return components.stream()
        .map(C::getClass)
        .map(componentClass -> (Class<? extends C>) componentClass)
        .collect(toList());
  }

  private Stream<C> getApplicableComponentsStream(P payload) {
    return orderedComponents.stream().filter(component -> component.isApplicable(payload));
  }

  private boolean isComponentPresentInProcessingOrder(C component) {
    List<Class<? extends C>> classesInProcessingOrder = getClassesInProcessingOrder();
    return !isEmpty(classesInProcessingOrder)
        && classesInProcessingOrder.contains(component.getClass());
  }

  private int getProcessingOrder(C component) {
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
