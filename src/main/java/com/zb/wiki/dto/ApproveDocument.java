package com.zb.wiki.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ApproveDocument {

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request{
    @NotNull
    @Pattern(regexp = "^(APPROVED|REJECTED)$",
        message = "Status는 APPROVED or REJECTED 만 가능합니다")
    private String status;
  }

}
