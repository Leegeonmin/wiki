package com.zb.wiki.dto;

import com.zb.wiki.elasticsearch.DocumentDocument;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentSearchDto {

  private Long id;
  private String title;
  private List<String> tags;
  private String createdBy;
  private LocalDateTime createdDateTime;

  public static DocumentSearchDto of(DocumentDocument document) {
    return DocumentSearchDto.builder()
        .id(document.getId())
        .title(document.getTitle())
        .tags(document.getTags())
        .createdBy(document.getCreatedBy())
        .createdDateTime(document.getCreatedDate())
        .build();
  }
}
