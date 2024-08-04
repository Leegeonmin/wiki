package com.zb.wiki.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class GlobalResponse<T> {
  private String status; //success , fail
  private String message;
  private T data;
  private String invalidField;
  private String errorMessage;
}
