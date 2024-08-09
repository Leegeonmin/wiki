package com.zb.wiki.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class KakaoOauth2 {
  @Getter
  @Builder
  @AllArgsConstructor
  public static class Response{
    private String accessToken;
  }

}
