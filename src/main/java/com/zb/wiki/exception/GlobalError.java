package com.zb.wiki.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalError {
  APPROVAL_PERIOD_EXPIRED("승인/미승인 기간이 지났습니다", HttpStatus.FORBIDDEN),
  USER_ALREADY_SUBMIT("이미 승인/미승인 참여한 사용자입니다", HttpStatus.CONFLICT),
  DOCUMENT_TYPE_ERROR("승인 대기중인 문서가 아닙니다", HttpStatus.FORBIDDEN),
  DOCUMENT_NOT_FOUND("문서 정보가 일치하지 않습니다", HttpStatus.NOT_FOUND),
  DOCUMENT_TITLE_DUPLICATED("문서의 제목이 이미 중복되어 있습니다", HttpStatus.BAD_REQUEST),
  RESOURCE_SERVER_ERROR("리소스 서버의 에러입니다", HttpStatus.INTERNAL_SERVER_ERROR),
  USERNAME_DUPLICATED("중복 아이디입니다", HttpStatus.CONFLICT),
  BAD_REQUEST("유효하지않은 요청입니다", HttpStatus.BAD_REQUEST),
  USER_NOT_FOUND("사용자 정보가 일치하지 않습니다", HttpStatus.NOT_FOUND);

  private final String description;
  private final HttpStatus httpStatus;

}
