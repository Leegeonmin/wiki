package com.zb.wiki.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

public class ApproveDocument {

  @Getter
  public static class Request{
    @NotNull
    @Pattern(regexp = "^(APPROVED|REJECTED)$",
        message = "Status는 APPROVED or REJECTED 만 가능합니다")
    private String status;
  }

}
