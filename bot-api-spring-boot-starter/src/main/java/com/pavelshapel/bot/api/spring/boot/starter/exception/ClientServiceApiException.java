package com.pavelshapel.bot.api.spring.boot.starter.exception;

public final class ClientServiceApiException extends RuntimeException {
  public ClientServiceApiException(String message, Throwable cause) {
    super(message, cause);
  }
}
