package com.pavelshapel.starter.boot.spring.ordered;

import java.util.function.Function;
import org.springframework.core.Ordered;

public abstract class OrderedComponent<PAYLOAD, RESULT>
    implements Function<PAYLOAD, RESULT>, Ordered {
  public static final int ZERO = 0;
  public static final int ONE = 1;
  public static final int TWO = 2;
  public static final int THREE = 3;
  public static final int FOUR = 4;
  public static final int FIVE = 5;
  public static final int SIX = 6;
  public static final int SEVEN = 7;
  public static final int EIGHT = 8;
  public static final int NINE = 9;
  public static final int TEN = 10;

  protected boolean isApplicable(PAYLOAD payload) {
    return true;
  }

  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }
}
