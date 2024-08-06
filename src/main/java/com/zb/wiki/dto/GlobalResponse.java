package com.zb.wiki.dto;

import com.zb.wiki.type.GlobalResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class GlobalResponse<T> {
  private GlobalResponseStatus status; //success , fail
  private String message;
  private T data;
  private String invalidField;
  private String inputErrorDetail;
}
