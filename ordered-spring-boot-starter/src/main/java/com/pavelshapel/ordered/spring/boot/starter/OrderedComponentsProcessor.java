package com.pavelshapel.ordered.spring.boot.starter;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class OrderedComponentsProcessor<P, R, C extends OrderedComponent<P, R>>
    implements Function<P, List<R>> {
  @Autowired private List<C> components;

  private List<C> orderedComponents;

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
    return orderedComponents.stream()
        .filter(component -> component.isApplicable(payload))
        .map(component -> component.apply(payload))
        .toList();
  }

  protected List<Class<? extends C>> getClassesInProcessingOrder() {
    return components.stream()
        .map(C::getClass)
        .map(componentClass -> (Class<? extends C>) componentClass)
        .collect(toList());
  }

  private boolean isComponentPresentInProcessingOrder(C component) {
    List<Class<? extends C>> classesInProcessingOrder = getClassesInProcessingOrder();
    return !isEmpty(classesInProcessingOrder)
        && classesInProcessingOrder.contains(component.getClass());
  }

  private int getProcessingOrder(C component) {
    return getClassesInProcessingOrder().indexOf(component.getClass());
  }
}
