package com.zb.wiki.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(GlobalException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(final GlobalException e) {
    log.error("CustomException occurred : ", e);

    return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(
        ErrorResponse.builder()
            .errorCode(e.getErrorCode())
            .message(e.getErrorMessage())
            .build()
    );
  }

  //요청 데이터 오류
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("MethodArgumentNotValidException is occurred", e);

    BindingResult bindingResult = e.getBindingResult();
    FieldError error = bindingResult.getFieldError();
    assert error != null;
    String fieldError = String.format("Field: %s, Error: %s", error.getField(), error.getDefaultMessage());

    return ResponseEntity.status(GlobalError.BAD_REQUEST.getHttpStatus()).body(
        ErrorResponse.builder()
            .errorCode(GlobalError.BAD_REQUEST)
            .message(GlobalError.BAD_REQUEST.getDescription())
            .fieldError(fieldError)
            .build()
    );
  }

  //RuntimeException 으로 뭔가 잘못된 데이터가 바인딩 되었을때 발생하는 에러이다.
  //SQL 문이 잘못되었거나 Data 가 잘못되었을경우
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    log.error("DataIntegrityViolationException occurred", e);
    return ResponseEntity.status(GlobalError.BAD_REQUEST.getHttpStatus()).body(
        ErrorResponse.builder()
            .errorCode(GlobalError.BAD_REQUEST)
            .message(GlobalError.BAD_REQUEST.getDescription())
            .build()
    );
  }

  @ResponseStatus
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Exception is occurred", e);
    return ResponseEntity.status(GlobalError.BAD_REQUEST.getHttpStatus()).body(
        ErrorResponse.builder()
            .errorCode(GlobalError.BAD_REQUEST)
            .message(GlobalError.BAD_REQUEST.getDescription())
            .build()
    );
  }
}
