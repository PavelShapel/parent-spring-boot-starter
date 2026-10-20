package com.pavelshapel.starter.boot.spring.ordered;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.aopalliance.intercept.Joinpoint;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.aop.framework.ProxyFactory;

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

  static class TestOrderedComponentsProcessor
      extends OrderedComponentsProcessor<String, String, TestOrderedComponent> {

    TestOrderedComponentsProcessor(List<TestOrderedComponent> components) {
      super(components);
    }

    @Override
    protected List<OrderedResult<String>> apply(String payload) {
      return List.of();
    }
  }

  static class CustomOrderTestOrderedComponentsProcessor extends TestOrderedComponentsProcessor {

    CustomOrderTestOrderedComponentsProcessor(List<TestOrderedComponent> components) {
      super(components);
    }

    @Override
    protected List<Class<? extends TestOrderedComponent>> getClassesInProcessingOrder() {
      return List.of(
          ApplicableTestOrderedComponent3.class, ApplicableTestOrderedComponent1.class);
    }
  }

  private TestOrderedComponentsProcessor sut;

  @BeforeEach
  void setUp() {
    sut =
        new TestOrderedComponentsProcessor(
            List.of(
                new ApplicableTestOrderedComponent1(),
                new NotApplicableTestOrderedComponent2(),
                new ApplicableTestOrderedComponent3()));
    sut.init();
  }

  @Test
  void getApplicableComponentsStreamOrdersAndFiltersByClassesInProcessingOrder() {
    String payload = "testPayload";
    var sut =
        new CustomOrderTestOrderedComponentsProcessor(
            List.of(
                new ApplicableTestOrderedComponent1(),
                new NotApplicableTestOrderedComponent2(),
                new ApplicableTestOrderedComponent3()));
    sut.init();

    List<TestOrderedComponent> result = sut.getApplicableComponentsStream(payload).toList();

    assertThat(result)
        .extracting(TestOrderedComponent::getId)
        .containsExactly("ApplicableTestOrderedComponent3", "ApplicableTestOrderedComponent1");
  }

  @Test
  void getSingleByIdResolvesProxiedComponentByUltimateTargetClass() {
    ProxyFactory factory = new ProxyFactory(new ApplicableTestOrderedComponent1());
    factory.setProxyTargetClass(/* proxyTargetClass= */ true);
    factory.addAdvice((MethodInterceptor) Joinpoint::proceed);
    TestOrderedComponent proxiedComponent = (TestOrderedComponent) factory.getProxy();
    var sut = new TestOrderedComponentsProcessor(List.of(proxiedComponent));
    sut.init();

    TestOrderedComponent result = sut.getSingleById("ApplicableTestOrderedComponent1");

    assertThat(result).isSameAs(proxiedComponent);
  }

  @Test
  void initThrowsWhenDuplicateComponentIdFound() {
    var component1 = new ApplicableTestOrderedComponent1();
    var component2 = new ApplicableTestOrderedComponent1();
    var sut = new TestOrderedComponentsProcessor(List.of(component1, component2));

    assertThatThrownBy(sut::init)
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Duplicate component id found: [ApplicableTestOrderedComponent1]");
  }

  @Test
  void getSingleReturnsSingleComponentWhenOnlyOneApplicable() {
    String payload = "testPayload";
    var sut =
        new TestOrderedComponentsProcessor(
            List.of(
                new ApplicableTestOrderedComponent1(), new NotApplicableTestOrderedComponent2()));
    sut.init();

    TestOrderedComponent result = sut.getSingle(payload);

    assertThat(result).isInstanceOf(ApplicableTestOrderedComponent1.class);
  }

  @Test
  void getSingleReturnsSingleComponentWhenPredicateMatchesOnlyOne() {
    String payload = "testPayload";
    Predicate<TestOrderedComponent> predicate = component -> component.getOrder() == 1;

    TestOrderedComponent result = sut.getSingle(payload, predicate);

    assertThat(result).isInstanceOf(ApplicableTestOrderedComponent1.class);
  }

  @ParameterizedTest
  @MethodSource("getSingleThrowsWhenApplicableComponentsCardinalityIsNotOneTestCasesProvider")
  void getSingleThrowsWhenApplicableComponentsCardinalityIsNotOne(
      GetSingleThrowsWhenApplicableComponentsCardinalityIsNotOneTestCase testCase) {
    String payload = "testPayload";
    List<TestOrderedComponent> components = testCase.components();
    String expectedMessage = testCase.expectedMessage();
    var sut = new TestOrderedComponentsProcessor(components);
    sut.init();

    assertThatThrownBy(() -> sut.getSingle(payload))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(expectedMessage);
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

  private static Stream<GetSingleThrowsWhenApplicableComponentsCardinalityIsNotOneTestCase>
      getSingleThrowsWhenApplicableComponentsCardinalityIsNotOneTestCasesProvider() {
    return Stream.of(
        new GetSingleThrowsWhenApplicableComponentsCardinalityIsNotOneTestCase(
            List.of(new NotApplicableTestOrderedComponent2()),
            "Expected exactly 1 element, got 0"),
        new GetSingleThrowsWhenApplicableComponentsCardinalityIsNotOneTestCase(
            List.of(
                new ApplicableTestOrderedComponent1(), new ApplicableTestOrderedComponent3()),
            "Expected exactly 1 element, got 2"));
  }

  private static Stream<GetSingleByIdThrowsWhenNotFoundTestCase>
      getSingleByIdThrowsWhenNotFoundTestCasesProvider() {
    return Stream.of(
        new GetSingleByIdThrowsWhenNotFoundTestCase(
            "NonExistentId", "No component found for id [NonExistentId]"),
        new GetSingleByIdThrowsWhenNotFoundTestCase(
            /* id= */ null, "No component found for id [null]"));
  }

  private record GetSingleThrowsWhenApplicableComponentsCardinalityIsNotOneTestCase(
      List<TestOrderedComponent> components, String expectedMessage) {}

  private record GetSingleByIdThrowsWhenNotFoundTestCase(String id, String expectedMessage) {}
}
