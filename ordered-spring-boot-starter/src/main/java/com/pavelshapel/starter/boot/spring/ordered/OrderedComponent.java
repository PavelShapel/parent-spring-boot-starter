package com.pavelshapel.starter.boot.spring.ordered;

import java.util.function.Function;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.core.Ordered;

public abstract class OrderedComponent<PAYLOAD, RESULT>
    implements Function<PAYLOAD, OrderedResult<RESULT>>, Ordered {
  @Override
  public OrderedResult<RESULT> apply(PAYLOAD payload) {
    RESULT result = processPayload(payload);
    return new OrderedResult<>(getId(), result);
  }

  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }

  public String getId() {
    return getTargetClass().getSimpleName();
  }

  Class<?> getTargetClass() {
    return AopProxyUtils.ultimateTargetClass(this);
  }

  protected abstract RESULT processPayload(PAYLOAD payload);

  protected boolean isApplicable(PAYLOAD payload) {
    return true;
  }
}
