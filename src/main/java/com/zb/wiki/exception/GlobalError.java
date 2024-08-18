package com.zb.wiki.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalError {
  DOCUMENT_TITLE_DUPLICATED("문서의 제목이 이미 중복되어 있습니다", HttpStatus.BAD_REQUEST),
  RESOURCE_SERVER_ERROR("리소스 서버의 에러입니다", HttpStatus.INTERNAL_SERVER_ERROR),
  USERNAME_DUPLICATED("중복 아이디입니다", HttpStatus.CONFLICT),
  BAD_REQUEST("유효하지않은 요청입니다", HttpStatus.BAD_REQUEST),
  USER_NOT_FOUND("사용자 정보가 일치하지 않습니다", HttpStatus.NOT_FOUND);

  private final String description;
  private final HttpStatus httpStatus;

}
