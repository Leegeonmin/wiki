package com.zb.wiki.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException{
  private final GlobalError errorCode;
  private final String errorMessage;
  public GlobalException(GlobalError errorCode) {
    this.errorCode = errorCode;
    this.errorMessage = errorCode.getDescription();
  }
}
