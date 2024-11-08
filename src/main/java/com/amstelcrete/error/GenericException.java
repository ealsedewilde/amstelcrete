package com.amstelcrete.error;

public class GenericException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public GenericException(String message) {
    super(message);
  }

  public GenericException(Throwable cause) {
    super(cause);
  }

  public GenericException(String message, Throwable cause) {
    super(message, cause);
  }

  public GenericException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
