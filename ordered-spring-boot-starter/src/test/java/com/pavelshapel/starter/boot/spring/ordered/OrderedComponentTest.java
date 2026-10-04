package com.pavelshapel.starter.boot.spring.ordered;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

import java.util.stream.Stream;
import org.aopalliance.intercept.Joinpoint;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.aop.framework.ProxyFactory;

final class OrderedComponentTest {
  private static final String PAYLOAD = "test-payload";

  abstract static class StringOrderedComponent extends OrderedComponent<String, String> {}

  static class TestStringOrderedComponent extends StringOrderedComponent {
    @Override
    protected String processPayload(String payload) {
      return payload;
    }
  }

  private static StringOrderedComponent sut() {
    return new TestStringOrderedComponent();
  }

  private static StringOrderedComponent proxySut() {
    ProxyFactory factory = new ProxyFactory(sut());
    factory.setProxyTargetClass(/* proxyTargetClass= */ true);
    factory.addAdvice((MethodInterceptor) Joinpoint::proceed);
    return (StringOrderedComponent) factory.getProxy();
  }

  private static Stream<StringOrderedComponent> sutProvider() {
    return Stream.of(sut(), proxySut());
  }

  @ParameterizedTest
  @MethodSource("sutProvider")
  void applyReturnsOrderedResult(StringOrderedComponent sut) {
    OrderedResult<String> result = sut.apply(PAYLOAD);

    assertThat(result)
        .returns(PAYLOAD, OrderedResult::result)
        .returns("TestStringOrderedComponent", OrderedResult::orderedComponentId);
  }

  @ParameterizedTest
  @MethodSource("sutProvider")
  void isApplicableReturnsTrueByDefault(StringOrderedComponent sut) {
    boolean result = sut.isApplicable(PAYLOAD);

    assertThat(result).isTrue();
  }

  @ParameterizedTest
  @MethodSource("sutProvider")
  void getOrderReturnsLowestPrecedenceByDefault(StringOrderedComponent sut) {
    int result = sut.getOrder();

    assertThat(result).isEqualTo(LOWEST_PRECEDENCE);
  }

  @ParameterizedTest
  @MethodSource("sutProvider")
  void getTargetClassReturnsUltimateTargetClass(StringOrderedComponent sut) {
    Class<?> result = sut.getTargetClass();

    assertThat(result).isEqualTo(TestStringOrderedComponent.class);
  }

  @ParameterizedTest
  @MethodSource("sutProvider")
  void getIdReturnsTargetClassSimpleName(StringOrderedComponent sut) {
    String result = sut.getId();

    assertThat(result).isEqualTo("TestStringOrderedComponent");
  }
}
