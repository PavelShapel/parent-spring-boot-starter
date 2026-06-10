package com.pavelshapel.starter.boot.spring.bot.api.exception;

public final class ClientServiceApiException extends RuntimeException {
  public ClientServiceApiException(String message, Throwable cause) {
    super(message, cause);
  }
}
