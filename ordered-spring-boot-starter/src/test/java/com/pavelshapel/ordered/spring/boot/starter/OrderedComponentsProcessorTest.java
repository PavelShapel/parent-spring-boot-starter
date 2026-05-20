package com.pavelshapel.ordered.spring.boot.starter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@SpringBootTest(
    classes = {
      OrderedComponentsProcessorTest.ApplicableTestOrderedComponent1.class,
      OrderedComponentsProcessorTest.NotApplicableTestOrderedComponent2.class,
      OrderedComponentsProcessorTest.ApplicableTestOrderedComponent3.class,
      OrderedComponentsProcessorTest.TestOrderedComponentsProcessorInProcessingOrder.class,
      OrderedComponentsProcessorTest.TestOrderedComponentsProcessorInDefinedOrder.class
    })
class OrderedComponentsProcessorTest {
  abstract static class TestOrderedComponent extends OrderedComponent<String, String> {
    private final int order;
    private final boolean isApplicable;

    TestOrderedComponent(int order, boolean isApplicable) {
      this.order = order;
      this.isApplicable = isApplicable;
    }

    @Override
    public String apply(String payload) {
      return "%s%d".formatted(payload, order);
    }

    @Override
    protected boolean isApplicable(String payload) {
      return isApplicable;
    }

    @Override
    public int getOrder() {
      return order;
    }
  }

  @Component
  static class ApplicableTestOrderedComponent1 extends TestOrderedComponent {
    ApplicableTestOrderedComponent1() {
      super(/* order= */ 1, /* isApplicable= */ true);
    }
  }

  @Component
  static class NotApplicableTestOrderedComponent2 extends TestOrderedComponent {
    NotApplicableTestOrderedComponent2() {
      super(/* order= */ 2, /* isApplicable= */ false);
    }
  }

  @Component
  static class ApplicableTestOrderedComponent3 extends TestOrderedComponent {
    ApplicableTestOrderedComponent3() {
      super(/* order= */ 3, /* isApplicable= */ true);
    }
  }

  @Component
  static class TestOrderedComponentsProcessorInDefinedOrder
      extends OrderedComponentsProcessor<String, String, TestOrderedComponent> {

    protected TestOrderedComponentsProcessorInDefinedOrder(List<TestOrderedComponent> components) {
      super(components);
    }
  }

  @Component
  static class TestOrderedComponentsProcessorInProcessingOrder
      extends OrderedComponentsProcessor<String, String, TestOrderedComponent> {

    protected TestOrderedComponentsProcessorInProcessingOrder(
        List<TestOrderedComponent> components) {
      super(components);
    }

    @Override
    protected List<Class<? extends TestOrderedComponent>> getClassesInProcessingOrder() {
      return List.of(
          ApplicableTestOrderedComponent3.class,
          NotApplicableTestOrderedComponent2.class,
          ApplicableTestOrderedComponent1.class);
    }
  }

  @Autowired private TestOrderedComponentsProcessorInDefinedOrder inDefinedOrderProcessor;

  @Autowired private TestOrderedComponentsProcessorInProcessingOrder inProcessingOrderProcessor;

  @Test
  void shouldApplyOnlyApplicableComponentsInDefinedOrder() {
    List<String> result = inDefinedOrderProcessor.apply("testPayload");

    assertThat(result).containsExactly("testPayload1", "testPayload3");
  }

  @Test
  void shouldApplyAllComponentsInProcessingOrder() {
    List<String> result = inProcessingOrderProcessor.apply("testPayload");

    assertThat(result).containsExactly("testPayload3", "testPayload1");
  }

  @Test
  void shouldReturnSingleComponentWhenPredicateMatchesOnlyOne() {
    TestOrderedComponent result =
        inDefinedOrderProcessor.getSingle("testPayload", component -> component.getOrder() == 1);

    assertThat(result).isInstanceOf(ApplicableTestOrderedComponent1.class);
  }

  @Test
  void shouldThrowWhenMultipleApplicableComponentsPresent() {
    assertThatThrownBy(() -> inDefinedOrderProcessor.getSingle("testPayload"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Expected exactly 1 element");
  }

  @Test
  void shouldThrowWhenNoComponentMatchesPredicate() {
    assertThatThrownBy(
            () ->
                inDefinedOrderProcessor.getSingle(
                    "testPayload", component -> component.getOrder() == 99))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Expected exactly 1 element");
  }
}
