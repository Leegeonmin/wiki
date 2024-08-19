package com.zb.wiki.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class DocumentDto {
  private Long id;
  private String title;
  private List<String> tags;
  private String author;
}
