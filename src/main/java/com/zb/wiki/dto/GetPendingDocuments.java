package com.zb.wiki.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class GetPendingDocuments {

  @Builder
  @AllArgsConstructor
  @Getter
  public static class Response{
    private Long id;
    private String title;
    private List<String> tags;
    private String author;
  }

}
