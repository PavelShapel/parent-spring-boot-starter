package com.pavelshapel.starter.boot.spring.ordered;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

final class SequentialOrderedComponentsProcessorTest {
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

  static class OrderRecordingTestOrderedComponent1 extends ApplicableTestOrderedComponent1 {
    private final List<String> executionLog;

    OrderRecordingTestOrderedComponent1(List<String> executionLog) {
      this.executionLog = executionLog;
    }

    @Override
    protected String processPayload(String payload) {
      executionLog.add(getId());
      return super.processPayload(payload);
    }
  }

  static class OrderRecordingTestOrderedComponent3 extends ApplicableTestOrderedComponent3 {
    private final List<String> executionLog;

    OrderRecordingTestOrderedComponent3(List<String> executionLog) {
      this.executionLog = executionLog;
    }

    @Override
    protected String processPayload(String payload) {
      executionLog.add(getId());
      return super.processPayload(payload);
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
    var sut =
        new CustomOrderTestSequentialOrderedComponentsProcessor(
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
  void applyExecutesComponentsInStrictSequentialOrder() {
    String payload = "testPayload";
    List<String> executionLog = new ArrayList<>();
    var sut =
        new TestSequentialOrderedComponentsProcessor(
            List.of(
                new OrderRecordingTestOrderedComponent1(executionLog),
                new OrderRecordingTestOrderedComponent3(executionLog)));
    sut.init();

    sut.apply(payload);

    assertThat(executionLog)
        .containsExactly(
            "OrderRecordingTestOrderedComponent1", "OrderRecordingTestOrderedComponent3");
  }

  @ParameterizedTest
  @MethodSource("applyReturnsEmptyListWhenNoComponentsExecutedTestCasesProvider")
  void applyReturnsEmptyListWhenNoComponentsExecuted(
      ApplyReturnsEmptyListWhenNoComponentsExecutedTestCase testCase) {
    String payload = "testPayload";
    List<TestOrderedComponent> components = testCase.components();
    var sut = new TestSequentialOrderedComponentsProcessor(components);
    sut.init();

    List<OrderedResult<String>> result = sut.apply(payload);

    assertThat(result).isEmpty();
  }

  @Test
  void applyPropagatesExceptionAndHaltsExecutionWhenComponentFails() {
    String payload = "testPayload";
    var failingComponent = new FailingTestOrderedComponent();
    var trackingComponent = new TrackingTestOrderedComponent();
    var sut =
        new TestSequentialOrderedComponentsProcessor(List.of(failingComponent, trackingComponent));
    sut.init();

    assertAll(
        () ->
            assertThatThrownBy(() -> sut.apply(payload))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Execution failed for payload: testPayload"),
        () -> assertThat(trackingComponent.isExecuted()).isFalse());
  }

  @ParameterizedTest
  @MethodSource("applyFiltersComponentsBasedOnPayloadTestCasesProvider")
  void applyFiltersComponentsBasedOnPayload(ApplyFiltersComponentsBasedOnPayloadTestCase testCase) {
    String payload = testCase.payload();
    List<String> expectedComponentIds = testCase.expectedComponentIds();
    var sut =
        new TestSequentialOrderedComponentsProcessor(
            List.of(
                new ApplicableTestOrderedComponent1(
                    p -> p.startsWith("applicable1") || p.equals("all")),
                new ApplicableTestOrderedComponent3(
                    p -> p.startsWith("applicable3") || p.equals("all"))));
    sut.init();

    List<OrderedResult<String>> result = sut.apply(payload);

    assertThat(result)
        .extracting(OrderedResult::orderedComponentId)
        .containsExactlyElementsOf(expectedComponentIds);
  }

  private static Stream<ApplyReturnsEmptyListWhenNoComponentsExecutedTestCase>
      applyReturnsEmptyListWhenNoComponentsExecutedTestCasesProvider() {
    return Stream.of(
        new ApplyReturnsEmptyListWhenNoComponentsExecutedTestCase(List.of()),
        new ApplyReturnsEmptyListWhenNoComponentsExecutedTestCase(
            List.of(new NotApplicableTestOrderedComponent2())));
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

  private record ApplyReturnsEmptyListWhenNoComponentsExecutedTestCase(
      List<TestOrderedComponent> components) {}

  private record ApplyFiltersComponentsBasedOnPayloadTestCase(
      String payload, List<String> expectedComponentIds) {}
}
