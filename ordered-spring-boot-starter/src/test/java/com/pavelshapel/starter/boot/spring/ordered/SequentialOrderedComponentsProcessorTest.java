package com.pavelshapel.starter.boot.spring.ordered;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

final class SequentialOrderedComponentsProcessorTest {
  private static final int NON_EXISTENT_ORDER = 99;

  abstract static class TestOrderedComponent extends OrderedComponent<String, String> {
    private final int order;
    private final Predicate<String> applicabilityPredicate;

    TestOrderedComponent(int order, boolean isApplicable) {
      this(order, _ -> isApplicable);
    }

    TestOrderedComponent(int order, Predicate<String> applicabilityPredicate) {
      this.order = order;
      this.applicabilityPredicate = applicabilityPredicate;
    }

    @Override
    protected String processPayload(String payload) {
      return "%s%d".formatted(payload, order);
    }

    @Override
    protected boolean isApplicable(String payload) {
      return applicabilityPredicate.test(payload);
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

    ApplicableTestOrderedComponent1(Predicate<String> applicabilityPredicate) {
      super(/* order= */ 1, applicabilityPredicate);
    }
  }

  static class NotApplicableTestOrderedComponent2 extends TestOrderedComponent {
    NotApplicableTestOrderedComponent2() {
      super(/* order= */ 2, /* isApplicable= */ false);
    }

    NotApplicableTestOrderedComponent2(Predicate<String> applicabilityPredicate) {
      super(/* order= */ 2, applicabilityPredicate);
    }
  }

  static class ApplicableTestOrderedComponent3 extends TestOrderedComponent {
    ApplicableTestOrderedComponent3() {
      super(/* order= */ 3, /* isApplicable= */ true);
    }

    ApplicableTestOrderedComponent3(Predicate<String> applicabilityPredicate) {
      super(/* order= */ 3, applicabilityPredicate);
    }
  }

  static class FailingTestOrderedComponent extends TestOrderedComponent {
    FailingTestOrderedComponent() {
      super(/* order= */ 1, /* isApplicable= */ true);
    }

    @Override
    protected String processPayload(String payload) {
      throw new IllegalStateException("Execution failed for payload: " + payload);
    }
  }

  static class TrackingTestOrderedComponent extends TestOrderedComponent {
    private final AtomicBoolean executed = new AtomicBoolean(false);

    TrackingTestOrderedComponent() {
      super(/* order= */ 2, /* isApplicable= */ true);
    }

    @Override
    protected String processPayload(String payload) {
      executed.set(true);
      return super.processPayload(payload);
    }

    boolean isExecuted() {
      return executed.get();
    }
  }

  static class TestSequentialOrderedComponentsProcessor
      extends SequentialOrderedComponentsProcessor<String, String, TestOrderedComponent> {

    TestSequentialOrderedComponentsProcessor(List<TestOrderedComponent> components) {
      super(components);
    }
  }

  static class CustomOrderTestSequentialOrderedComponentsProcessor
      extends SequentialOrderedComponentsProcessor<String, String, TestOrderedComponent> {

    CustomOrderTestSequentialOrderedComponentsProcessor(List<TestOrderedComponent> components) {
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

  private TestSequentialOrderedComponentsProcessor sut;

  @BeforeEach
  void setUp() {
    sut =
        new TestSequentialOrderedComponentsProcessor(
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
  void applyAppliesApplicableComponentsInCustomProcessingOrder() {
    String payload = "testPayload";
    var customSut =
        new CustomOrderTestSequentialOrderedComponentsProcessor(
            List.of(
                new ApplicableTestOrderedComponent1(),
                new NotApplicableTestOrderedComponent2(),
                new ApplicableTestOrderedComponent3()));
    customSut.init();

    List<OrderedResult<String>> result = customSut.apply(payload);

    assertThat(result)
        .extracting(OrderedResult::orderedComponentId)
        .containsExactly("ApplicableTestOrderedComponent3", "ApplicableTestOrderedComponent1");
  }

  @Test
  void applyReturnsEmptyListWhenComponentsEmpty() {
    String payload = "testPayload";
    var emptySut = new TestSequentialOrderedComponentsProcessor(List.of());
    emptySut.init();

    List<OrderedResult<String>> result = emptySut.apply(payload);

    assertThat(result).isEmpty();
  }

  @Test
  void applyReturnsEmptyListWhenNoComponentApplicable() {
    String payload = "testPayload";
    var nonApplicableSut =
        new TestSequentialOrderedComponentsProcessor(
            List.of(new NotApplicableTestOrderedComponent2()));
    nonApplicableSut.init();

    List<OrderedResult<String>> result = nonApplicableSut.apply(payload);

    assertThat(result).isEmpty();
  }

  @Test
  void applyExecutesComponentsSequentially() {
    String payload = "testPayload";

    List<OrderedResult<String>> result = sut.apply(payload);

    assertThat(result)
        .extracting(OrderedResult::orderedComponentId)
        .containsExactly("ApplicableTestOrderedComponent1", "ApplicableTestOrderedComponent3");
  }

  @Test
  void applyPropagatesExceptionAndHaltsExecutionWhenComponentFails() {
    String payload = "testPayload";
    var failingComponent = new FailingTestOrderedComponent();
    var trackingComponent = new TrackingTestOrderedComponent();
    var failingSut =
        new TestSequentialOrderedComponentsProcessor(List.of(failingComponent, trackingComponent));
    failingSut.init();

    assertAll(
        () ->
            assertThatThrownBy(() -> failingSut.apply(payload))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Execution failed for payload: testPayload"),
        () -> assertThat(trackingComponent.isExecuted()).isFalse());
  }

  @ParameterizedTest
  @MethodSource("applyFiltersComponentsBasedOnPayloadTestCasesProvider")
  void applyFiltersComponentsBasedOnPayload(ApplyFiltersComponentsBasedOnPayloadTestCase testCase) {
    String payload = testCase.payload();
    List<String> expectedComponentIds = testCase.expectedComponentIds();
    var conditionalSut =
        new TestSequentialOrderedComponentsProcessor(
            List.of(
                new ApplicableTestOrderedComponent1(
                    p -> p.startsWith("applicable1") || p.equals("all")),
                new ApplicableTestOrderedComponent3(
                    p -> p.startsWith("applicable3") || p.equals("all"))));
    conditionalSut.init();

    List<OrderedResult<String>> result = conditionalSut.apply(payload);

    assertThat(result)
        .extracting(OrderedResult::orderedComponentId)
        .containsExactlyElementsOf(expectedComponentIds);
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
    var nonApplicableSut =
        new TestSequentialOrderedComponentsProcessor(
            List.of(new NotApplicableTestOrderedComponent2()));
    nonApplicableSut.init();

    assertThatThrownBy(() -> nonApplicableSut.getSingle(payload))
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
    var duplicateSut =
        new TestSequentialOrderedComponentsProcessor(List.of(component1, component2));

    assertThatThrownBy(duplicateSut::init)
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Duplicate component id found: [ApplicableTestOrderedComponent1]");
  }

  private static Stream<ApplyFiltersComponentsBasedOnPayloadTestCase>
      applyFiltersComponentsBasedOnPayloadTestCasesProvider() {
    return Stream.of(
        new ApplyFiltersComponentsBasedOnPayloadTestCase(
            "all", List.of("ApplicableTestOrderedComponent1", "ApplicableTestOrderedComponent3")),
        new ApplyFiltersComponentsBasedOnPayloadTestCase(
            "applicable1Only", List.of("ApplicableTestOrderedComponent1")),
        new ApplyFiltersComponentsBasedOnPayloadTestCase(
            "applicable3Only", List.of("ApplicableTestOrderedComponent3")),
        new ApplyFiltersComponentsBasedOnPayloadTestCase("none", List.of()));
  }

  private static Stream<GetSingleByIdThrowsWhenNotFoundTestCase>
      getSingleByIdThrowsWhenNotFoundTestCasesProvider() {
    return Stream.of(
        new GetSingleByIdThrowsWhenNotFoundTestCase(
            "NonExistentId", "No component found for id [NonExistentId]"),
        new GetSingleByIdThrowsWhenNotFoundTestCase(
            /* id= */ null, "No component found for id [null]"));
  }

  private record ApplyFiltersComponentsBasedOnPayloadTestCase(
      String payload, List<String> expectedComponentIds) {}

  private record GetSingleByIdThrowsWhenNotFoundTestCase(String id, String expectedMessage) {}
}
