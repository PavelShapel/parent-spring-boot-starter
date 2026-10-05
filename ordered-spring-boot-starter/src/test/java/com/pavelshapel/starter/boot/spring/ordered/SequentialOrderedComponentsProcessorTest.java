package com.pavelshapel.starter.boot.spring.ordered;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class SequentialOrderedComponentsProcessorTest {
  private static final int NON_EXISTENT_ORDER = 99;

  abstract static class TestOrderedComponent extends OrderedComponent<String, String> {
    private final int order;
    private final Predicate<String> applicabilityPredicate;

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

  static class FirstTestOrderedComponent extends TestOrderedComponent {
    FirstTestOrderedComponent() {
      this(_ -> true);
    }

    FirstTestOrderedComponent(Predicate<String> applicabilityPredicate) {
      super(/* order= */ 1, applicabilityPredicate);
    }
  }

  static class SecondTestOrderedComponent extends TestOrderedComponent {
    SecondTestOrderedComponent() {
      this(_ -> false);
    }

    SecondTestOrderedComponent(Predicate<String> applicabilityPredicate) {
      super(/* order= */ 2, applicabilityPredicate);
    }
  }

  static class ThirdTestOrderedComponent extends TestOrderedComponent {
    ThirdTestOrderedComponent() {
      this(_ -> true);
    }

    ThirdTestOrderedComponent(Predicate<String> applicabilityPredicate) {
      super(/* order= */ 3, applicabilityPredicate);
    }
  }

  static class LoggingFirstTestOrderedComponent extends TestOrderedComponent {
    private final List<String> executionLog;

    LoggingFirstTestOrderedComponent(List<String> executionLog) {
      super(/* order= */ 1, _ -> true);
      this.executionLog = executionLog;
    }

    @Override
    protected String processPayload(String payload) {
      executionLog.add(getId());
      return super.processPayload(payload);
    }
  }

  static class LoggingThirdTestOrderedComponent extends TestOrderedComponent {
    private final List<String> executionLog;

    LoggingThirdTestOrderedComponent(List<String> executionLog) {
      super(/* order= */ 3, _ -> true);
      this.executionLog = executionLog;
    }

    @Override
    protected String processPayload(String payload) {
      executionLog.add(getId());
      return super.processPayload(payload);
    }
  }

  static class FailingTestOrderedComponent extends TestOrderedComponent {
    FailingTestOrderedComponent() {
      super(/* order= */ 1, _ -> true);
    }

    @Override
    protected String processPayload(String payload) {
      throw new IllegalStateException("Execution failed for payload: " + payload);
    }
  }

  static class TrackingTestOrderedComponent extends TestOrderedComponent {
    private final AtomicBoolean executed = new AtomicBoolean(false);

    TrackingTestOrderedComponent() {
      super(/* order= */ 2, _ -> true);
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
          ThirdTestOrderedComponent.class,
          SecondTestOrderedComponent.class,
          FirstTestOrderedComponent.class);
    }
  }

  private TestSequentialOrderedComponentsProcessor sut;

  @BeforeEach
  void setUp() {
    sut =
        new TestSequentialOrderedComponentsProcessor(
            List.of(
                new FirstTestOrderedComponent(),
                new SecondTestOrderedComponent(),
                new ThirdTestOrderedComponent()));
    sut.init();
  }

  @Test
  void applyAppliesOnlyApplicableComponentsInDefinedOrder() {
    String payload = "testPayload";

    List<OrderedResult<String>> result = sut.apply(payload);

    assertThat(result)
        .containsExactly(
            new OrderedResult<>("FirstTestOrderedComponent", "testPayload1"),
            new OrderedResult<>("ThirdTestOrderedComponent", "testPayload3"));
  }

  @Test
  void applyAppliesApplicableComponentsInCustomProcessingOrder() {
    String payload = "testPayload";
    var customSut =
        new CustomOrderTestSequentialOrderedComponentsProcessor(
            List.of(
                new FirstTestOrderedComponent(),
                new SecondTestOrderedComponent(),
                new ThirdTestOrderedComponent()));
    customSut.init();

    List<OrderedResult<String>> result = customSut.apply(payload);

    assertThat(result)
        .containsExactly(
            new OrderedResult<>("ThirdTestOrderedComponent", "testPayload3"),
            new OrderedResult<>("FirstTestOrderedComponent", "testPayload1"));
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
        new TestSequentialOrderedComponentsProcessor(List.of(new SecondTestOrderedComponent()));
    nonApplicableSut.init();

    List<OrderedResult<String>> result = nonApplicableSut.apply(payload);

    assertThat(result).isEmpty();
  }

  @Test
  void applyExecutesComponentsSequentially() {
    String payload = "testPayload";
    List<String> executionLog = new ArrayList<>();
    var loggingSut =
        new TestSequentialOrderedComponentsProcessor(
            List.of(
                new LoggingFirstTestOrderedComponent(executionLog),
                new LoggingThirdTestOrderedComponent(executionLog)));
    loggingSut.init();

    List<OrderedResult<String>> result = loggingSut.apply(payload);

    assertAll(
        () ->
            assertThat(executionLog)
                .containsExactly(
                    "LoggingFirstTestOrderedComponent", "LoggingThirdTestOrderedComponent"),
        () -> assertThat(result).hasSize(2));
  }

  @Test
  void applyPropagatesExceptionAndHaltsExecutionWhenComponentFails() {
    String payload = "testPayload";
    var failingComponent = new FailingTestOrderedComponent();
    var trackingComponent = new TrackingTestOrderedComponent();
    var failingSut =
        new TestSequentialOrderedComponentsProcessor(
            List.of(failingComponent, trackingComponent));
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
  void applyFiltersComponentsBasedOnPayload(
      ApplyFiltersComponentsBasedOnPayloadTestCase testCase) {
    String payload = testCase.payload();
    List<OrderedResult<String>> expectedResults = testCase.expectedResults();
    var conditionalSut =
        new TestSequentialOrderedComponentsProcessor(
            List.of(
                new FirstTestOrderedComponent(p -> p.startsWith("first") || p.equals("all")),
                new ThirdTestOrderedComponent(p -> p.startsWith("third") || p.equals("all"))));
    conditionalSut.init();

    List<OrderedResult<String>> result = conditionalSut.apply(payload);

    assertThat(result).isEqualTo(expectedResults);
  }

  @Test
  void getSingleReturnsSingleComponentWhenPredicateMatchesOnlyOne() {
    String payload = "testPayload";
    Predicate<TestOrderedComponent> predicate = component -> component.getOrder() == 1;

    TestOrderedComponent result = sut.getSingle(payload, predicate);

    assertThat(result).isInstanceOf(FirstTestOrderedComponent.class);
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
        new TestSequentialOrderedComponentsProcessor(List.of(new SecondTestOrderedComponent()));
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
    String id = "FirstTestOrderedComponent";

    TestOrderedComponent result = sut.getSingleById(id);

    assertThat(result).isInstanceOf(FirstTestOrderedComponent.class);
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
    var component1 = new FirstTestOrderedComponent();
    var component2 = new FirstTestOrderedComponent();
    var duplicateSut =
        new TestSequentialOrderedComponentsProcessor(List.of(component1, component2));

    assertThatThrownBy(duplicateSut::init)
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Duplicate component id found: [FirstTestOrderedComponent]");
  }

  private static Stream<ApplyFiltersComponentsBasedOnPayloadTestCase>
      applyFiltersComponentsBasedOnPayloadTestCasesProvider() {
    return Stream.of(
        new ApplyFiltersComponentsBasedOnPayloadTestCase(
            "all",
            List.of(
                new OrderedResult<>("FirstTestOrderedComponent", "all1"),
                new OrderedResult<>("ThirdTestOrderedComponent", "all3"))),
        new ApplyFiltersComponentsBasedOnPayloadTestCase(
            "firstOnly",
            List.of(new OrderedResult<>("FirstTestOrderedComponent", "firstOnly1"))),
        new ApplyFiltersComponentsBasedOnPayloadTestCase(
            "thirdOnly",
            List.of(new OrderedResult<>("ThirdTestOrderedComponent", "thirdOnly3"))),
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
      String payload, List<OrderedResult<String>> expectedResults) {}

  private record GetSingleByIdThrowsWhenNotFoundTestCase(
      String id, String expectedMessage) {}
}
