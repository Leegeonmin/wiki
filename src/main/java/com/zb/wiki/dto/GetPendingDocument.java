package com.zb.wiki.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class GetPendingDocument {
  @Getter
  @Builder
  @AllArgsConstructor
  public static class Response{
    private Long id;
    private String title;
    private String context;
    private List<String> tags;
    private String author;
  }

}
