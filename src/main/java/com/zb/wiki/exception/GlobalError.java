package com.zb.wiki.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalError {
  BAD_REQUEST("유효하지않은 요청입니다", HttpStatus.BAD_REQUEST),
  USER_NOT_FOUND("사용자 정보가 일치하지 않습니다", HttpStatus.NOT_FOUND);

  private final String description;
  private final HttpStatus httpStatus;

}
