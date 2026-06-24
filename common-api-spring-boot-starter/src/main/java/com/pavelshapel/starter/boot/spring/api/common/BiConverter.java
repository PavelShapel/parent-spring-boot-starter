package com.pavelshapel.starter.boot.spring.api.common;

@FunctionalInterface
public interface BiConverter<SOURCE, TARGET> {
  TARGET convertForward(SOURCE source);

  default SOURCE convertBackward(TARGET target) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  default BiConverter<TARGET, SOURCE> reverse() {
    return new BiConverter<>() {
      @Override
      public SOURCE convertForward(TARGET source) {
        return BiConverter.this.convertBackward(source);
      }

      @Override
      public TARGET convertBackward(SOURCE target) {
        return BiConverter.this.convertForward(target);
      }
    };
  }
}
