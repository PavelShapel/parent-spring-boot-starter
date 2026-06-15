package com.pavelshapel.starter.boot.spring.bot.api;

@FunctionalInterface
public interface BiConverter<S, T> {
  T convertForward(S source);

  default S convertBackward(T target) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  default BiConverter<T, S> reverse() {
    return new BiConverter<>() {
      @Override
      public S convertForward(T source) {
        return BiConverter.this.convertBackward(source);
      }

      @Override
      public T convertBackward(S target) {
        return BiConverter.this.convertForward(target);
      }
    };
  }
}
