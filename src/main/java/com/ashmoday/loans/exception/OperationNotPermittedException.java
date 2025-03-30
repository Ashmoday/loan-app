package com.ashmoday.loans.exception;

public class OperationNotPermittedException extends RuntimeException {

  public OperationNotPermittedException(String msg) {
    super(msg);
  }

}
