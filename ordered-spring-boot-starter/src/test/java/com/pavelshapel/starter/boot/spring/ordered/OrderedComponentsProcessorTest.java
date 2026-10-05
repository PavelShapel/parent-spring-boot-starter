package com.pavelshapel.starter.boot.spring.ordered;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

final class OrderedComponentsProcessorTest {
  private static final int NON_EXISTENT_ORDER = 99;

  abstract static class TestOrderedComponent extends OrderedComponent<String, String> {
    private final int order;
    private final boolean isApplicable;

    TestOrderedComponent(int order, boolean isApplicable) {
      this.order = order;
      this.isApplicable = isApplicable;
    }

    @Override
    protected String processPayload(String payload) {
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

  static class ApplicableTestOrderedComponent1 extends TestOrderedComponent {
    ApplicableTestOrderedComponent1() {
      super(/* order= */ 1, /* isApplicable= */ true);
    }
  }

  static class NotApplicableTestOrderedComponent2 extends TestOrderedComponent {
    NotApplicableTestOrderedComponent2() {
      super(/* order= */ 2, /* isApplicable= */ false);
    }
  }

  static class ApplicableTestOrderedComponent3 extends TestOrderedComponent {
    ApplicableTestOrderedComponent3() {
      super(/* order= */ 3, /* isApplicable= */ true);
    }
  }

  static class TestSequentialOrderedComponentsProcessorInDefinedOrder
      extends SequentialOrderedComponentsProcessor<String, String, TestOrderedComponent> {

    TestSequentialOrderedComponentsProcessorInDefinedOrder(List<TestOrderedComponent> components) {
      super(components);
    }
  }

  static class TestSequentialOrderedComponentsProcessorInProcessingOrder
      extends SequentialOrderedComponentsProcessor<String, String, TestOrderedComponent> {

    TestSequentialOrderedComponentsProcessorInProcessingOrder(
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

  static class TestParallelOrderedComponentsProcessor
      extends ParallelOrderedComponentsProcessor<String, String, TestOrderedComponent> {

    TestParallelOrderedComponentsProcessor(List<TestOrderedComponent> components) {
      super(components);
    }
  }

  private TestSequentialOrderedComponentsProcessorInDefinedOrder sut;

  @BeforeEach
  void setUp() {
    sut =
        new TestSequentialOrderedComponentsProcessorInDefinedOrder(
            List.of(
                new ApplicableTestOrderedComponent1(),
                new NotApplicableTestOrderedComponent2(),
                new ApplicableTestOrderedComponent3()));
    sut.init();
  }

  @Test
  void applyAppliesOnlyApplicableComponentsInDefinedOrder() {
    String payload = "testPayload";

    List<OrderedResult<String>> result = sut.apply(payload);

    assertThat(result)
        .containsExactly(
            new OrderedResult<>("ApplicableTestOrderedComponent1", "testPayload1"),
            new OrderedResult<>("ApplicableTestOrderedComponent3", "testPayload3"));
  }

  @Test
  void applyAppliesAllComponentsInProcessingOrder() {
    String payload = "testPayload";
    var sut =
        new TestSequentialOrderedComponentsProcessorInProcessingOrder(
            List.of(
                new ApplicableTestOrderedComponent1(),
                new NotApplicableTestOrderedComponent2(),
                new ApplicableTestOrderedComponent3()));
    sut.init();

    List<OrderedResult<String>> result = sut.apply(payload);

    assertThat(result)
        .extracting(OrderedResult::orderedComponentId)
        .containsExactly("ApplicableTestOrderedComponent3", "ApplicableTestOrderedComponent1");
  }

  @Test
  void applyAppliesInParallelProcessor() {
    String payload = "testPayload";
    var sut =
        new TestParallelOrderedComponentsProcessor(
            List.of(
                new ApplicableTestOrderedComponent1(),
                new NotApplicableTestOrderedComponent2(),
                new ApplicableTestOrderedComponent3()));
    sut.init();

    List<OrderedResult<String>> result = sut.apply(payload);

    assertThat(result)
        .extracting(OrderedResult::orderedComponentId)
        .containsExactly("ApplicableTestOrderedComponent1", "ApplicableTestOrderedComponent3");
  }

  @Test
  void getSingleReturnsSingleComponentWhenPredicateMatchesOnlyOne() {
    String payload = "testPayload";
    Predicate<TestOrderedComponent> predicate = component -> component.getOrder() == 1;

    TestOrderedComponent result = sut.getSingle(payload, predicate);

    assertThat(result).isInstanceOf(ApplicableTestOrderedComponent1.class);
  }

  @Test
  void getSingleThrowsWhenMultipleApplicableComponentsPresent() {
    String payload = "testPayload";

    assertThatThrownBy(() -> sut.getSingle(payload))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Expected exactly 1 element, got 2");
  }

  @Test
  void getSingleThrowsWhenNoComponentIsApplicable() {
    String payload = "testPayload";
    var notApplicableComponent = new NotApplicableTestOrderedComponent2();
    var sut =
        new SequentialOrderedComponentsProcessor<String, String, TestOrderedComponent>(
            List.of(notApplicableComponent)) {};
    sut.init();

    assertThatThrownBy(() -> sut.getSingle(payload))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Expected exactly 1 element, got 0");
  }

  @Test
  void getSingleThrowsWhenNoComponentMatchesPredicate() {
    String payload = "testPayload";
    Predicate<TestOrderedComponent> predicate =
        component -> component.getOrder() == NON_EXISTENT_ORDER;

    assertThatThrownBy(() -> sut.getSingle(payload, predicate))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Expected exactly 1 element, got 0");
  }

  @Test
  void getSingleByIdReturnsSingleComponentWhenExists() {
    String id = "ApplicableTestOrderedComponent1";

    TestOrderedComponent result = sut.getSingleById(id);

    assertThat(result).isInstanceOf(ApplicableTestOrderedComponent1.class);
  }

  @ParameterizedTest
  @MethodSource("getSingleByIdThrowsWhenNotFoundTestCasesProvider")
  void getSingleByIdThrowsWhenNotFound(GetSingleByIdThrowsWhenNotFoundTestCase testCase) {
    String id = testCase.id();
    String expectedMessage = testCase.expectedMessage();

    assertThatThrownBy(() -> sut.getSingleById(id))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage(expectedMessage);
  }

  @Test
  void initThrowsWhenDuplicateComponentIdFound() {
    var component1 = new ApplicableTestOrderedComponent1();
    var component2 = new ApplicableTestOrderedComponent1();
    var sut =
        new SequentialOrderedComponentsProcessor<String, String, TestOrderedComponent>(
            List.of(component1, component2)) {};

    assertThatThrownBy(sut::init)
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Duplicate component id found: [ApplicableTestOrderedComponent1]");
  }

  private static Stream<GetSingleByIdThrowsWhenNotFoundTestCase>
      getSingleByIdThrowsWhenNotFoundTestCasesProvider() {
    return Stream.of(
        new GetSingleByIdThrowsWhenNotFoundTestCase(
            "NonExistentId", "No component found for id [NonExistentId]"),
        new GetSingleByIdThrowsWhenNotFoundTestCase(
            /* id= */ null, "No component found for id [null]"));
  }

  private record GetSingleByIdThrowsWhenNotFoundTestCase(String id, String expectedMessage) {}
}
