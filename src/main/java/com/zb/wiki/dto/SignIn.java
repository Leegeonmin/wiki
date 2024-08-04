package com.zb.wiki.dto;


import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class SignIn {
  @Getter
  public static class Request{
    private String username;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
        message = "비밀번호는 최소 8자 이상이어야 하며, 영문자, 숫자, 특수문자(@$!%*#?&)를 각각 1개 이상 포함해야 합니다.")
    private String password;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class Response{
    private String accessToken;
  }
}
