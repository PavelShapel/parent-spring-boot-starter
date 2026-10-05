package com.pavelshapel.starter.boot.spring.ordered;

import static com.pavelshapel.starter.boot.spring.api.common.StreamCollectors.toSingle;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

abstract class OrderedComponentsProcessor<
    PAYLOAD, RESULT, COMPONENT extends OrderedComponent<PAYLOAD, RESULT>> {
  private final List<COMPONENT> components;

  private List<COMPONENT> orderedComponents;

  private Map<String, COMPONENT> orderedComponentsById;

  protected OrderedComponentsProcessor(List<COMPONENT> components) {
    this.components = components;
  }

  @PostConstruct
  protected void init() {
    List<Class<? extends COMPONENT>> classesInProcessingOrder = getClassesInProcessingOrder();
    Map<Class<?>, Integer> processingOrder =
        IntStream.range(0, classesInProcessingOrder.size())
            .boxed()
            .collect(toMap(classesInProcessingOrder::get, identity(), (existing, _) -> existing));
    orderedComponents =
        components.stream()
            .filter(component -> processingOrder.containsKey(component.getTargetClass()))
            .sorted(comparing(component -> processingOrder.get(component.getTargetClass())))
            .toList();
    orderedComponentsById =
        orderedComponents.stream()
            .collect(
                toMap(
                    COMPONENT::getId,
                    identity(),
                    (existing, _) -> {
                      throw new IllegalStateException(
                          "Duplicate component id found: [%s]".formatted(existing.getId()));
                    }));
  }

  protected abstract List<OrderedResult<RESULT>> apply(PAYLOAD payload);

  protected final COMPONENT getSingle(PAYLOAD payload) {
    return getSingle(payload, _ -> true);
  }

  protected final COMPONENT getSingle(PAYLOAD payload, Predicate<COMPONENT> predicate) {
    return getApplicableComponentsStream(payload).filter(predicate).collect(toSingle());
  }

  protected final COMPONENT getSingleById(String id) {
    return Optional.ofNullable(id)
        .map(orderedComponentsById::get)
        .orElseThrow(
            () -> new NoSuchElementException("No component found for id [%s]".formatted(id)));
  }

  @SuppressWarnings("unchecked")
  protected List<Class<? extends COMPONENT>> getClassesInProcessingOrder() {
    return components.stream()
        .<Class<? extends COMPONENT>>map(
            component -> (Class<? extends COMPONENT>) component.getTargetClass())
        .toList();
  }

  protected final Stream<COMPONENT> getApplicableComponentsStream(PAYLOAD payload) {
    return orderedComponents.stream().filter(component -> component.isApplicable(payload));
  }
}
