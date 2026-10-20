package com.pavelshapel.starter.boot.spring.ordered;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

final class ParallelOrderedComponentsProcessorTest {
  private static final Duration COMPONENT_DELAY = Duration.ofMillis(200);

  abstract static class TestOrderedComponent extends OrderedComponent<String, String> {
    private final int order;
    private final Predicate<String> applicabilityPredicate;
    private final Duration delay;

    TestOrderedComponent(int order, boolean isApplicable) {
      this(order, _ -> isApplicable, Duration.ZERO);
    }

    TestOrderedComponent(int order, Predicate<String> applicabilityPredicate) {
      this(order, applicabilityPredicate, Duration.ZERO);
    }

    TestOrderedComponent(int order, boolean isApplicable, Duration delay) {
      this(order, _ -> isApplicable, delay);
    }

    TestOrderedComponent(int order, Predicate<String> applicabilityPredicate, Duration delay) {
      this.order = order;
      this.applicabilityPredicate = applicabilityPredicate;
      this.delay = delay;
    }

    @Override
    protected String processPayload(String payload) {
      sleep(delay);
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

    ApplicableTestOrderedComponent1(Duration delay) {
      super(/* order= */ 1, /* isApplicable= */ true, delay);
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

    ApplicableTestOrderedComponent3(Duration delay) {
      super(/* order= */ 3, /* isApplicable= */ true, delay);
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

  static class TestParallelOrderedComponentsProcessor
      extends ParallelOrderedComponentsProcessor<String, String, TestOrderedComponent> {

    TestParallelOrderedComponentsProcessor(List<TestOrderedComponent> components) {
      super(components);
    }
  }

  static class CustomOrderTestParallelOrderedComponentsProcessor
      extends ParallelOrderedComponentsProcessor<String, String, TestOrderedComponent> {

    CustomOrderTestParallelOrderedComponentsProcessor(List<TestOrderedComponent> components) {
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

  private TestParallelOrderedComponentsProcessor sut;

  @BeforeEach
  void setUp() {
    sut =
        new TestParallelOrderedComponentsProcessor(
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
        new CustomOrderTestParallelOrderedComponentsProcessor(
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
  void applyPreservesResultOrderWhenLaterComponentFinishesFirst() {
    String payload = "testPayload";
    var sut =
        new TestParallelOrderedComponentsProcessor(
            List.of(
                new ApplicableTestOrderedComponent1(COMPONENT_DELAY),
                new ApplicableTestOrderedComponent3()));
    sut.init();

    List<OrderedResult<String>> result = sut.apply(payload);

    assertThat(result)
        .containsExactly(
            new OrderedResult<>("ApplicableTestOrderedComponent1", "testPayload1"),
            new OrderedResult<>("ApplicableTestOrderedComponent3", "testPayload3"));
  }

  @Test
  void applyExecutesComponentsInParallelWithinSingleComponentDelay() {
    String payload = "testPayload";
    var sut =
        new TestParallelOrderedComponentsProcessor(
            List.of(
                new ApplicableTestOrderedComponent1(COMPONENT_DELAY),
                new ApplicableTestOrderedComponent3(COMPONENT_DELAY)));
    sut.init();

    long result = measureTimeMillis(() -> sut.apply(payload));

    assertThat(result).isCloseTo(COMPONENT_DELAY.toMillis(), within(100L));
  }

  @ParameterizedTest
  @MethodSource("applyReturnsEmptyListWhenNoComponentsExecutedTestCasesProvider")
  void applyReturnsEmptyListWhenNoComponentsExecuted(
      ApplyReturnsEmptyListWhenNoComponentsExecutedTestCase testCase) {
    String payload = "testPayload";
    List<TestOrderedComponent> components = testCase.components();
    var sut = new TestParallelOrderedComponentsProcessor(components);
    sut.init();

    List<OrderedResult<String>> result = sut.apply(payload);

    assertThat(result).isEmpty();
  }

  @Test
  void applyThrowsCompletionExceptionWhenComponentFails() {
    String payload = "testPayload";
    var sut =
        new TestParallelOrderedComponentsProcessor(
            List.of(new FailingTestOrderedComponent(), new ApplicableTestOrderedComponent3()));
    sut.init();

    assertThatThrownBy(() -> sut.apply(payload))
        .isInstanceOf(CompletionException.class)
        .hasCauseInstanceOf(IllegalStateException.class)
        .hasRootCauseMessage("Execution failed for payload: testPayload");
  }

  @ParameterizedTest
  @MethodSource("applyFiltersComponentsBasedOnPayloadTestCasesProvider")
  void applyFiltersComponentsBasedOnPayload(ApplyFiltersComponentsBasedOnPayloadTestCase testCase) {
    String payload = testCase.payload();
    List<String> expectedComponentIds = testCase.expectedComponentIds();
    var sut =
        new TestParallelOrderedComponentsProcessor(
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

  private static long measureTimeMillis(Runnable action) {
    long startNanos = System.nanoTime();
    action.run();
    return NANOSECONDS.toMillis(System.nanoTime() - startNanos);
  }

  private static void sleep(Duration duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Interrupted while sleeping", exception);
    }
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
