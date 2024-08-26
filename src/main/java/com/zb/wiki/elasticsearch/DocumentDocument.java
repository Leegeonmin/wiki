package com.zb.wiki.elasticsearch;

import com.zb.wiki.domain.Document;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@org.springframework.data.elasticsearch.annotations.Document(indexName = "documents")
public class DocumentDocument {
  @Id
  @Field(type = FieldType.Long)
  private Long id;
  @Field(type = FieldType.Text)
  private String title;
  @Field(type = FieldType.Text)
  private String context;
  @Field(type = FieldType.Keyword)
  private List<String> tags;
  @Field(type = FieldType.Text)
  private String createdBy;
  @Field(type = FieldType.Date)
  private LocalDateTime createdDate;

  public static DocumentDocument from(Document document) {
    return DocumentDocument.builder()
        .id(document.getId())
        .title(document.getTitle())
        .context(document.getContext())
        .tags( List.of(document.getTag().split("\\|")))
        .createdBy(document.getCreatedBy().getUsername())
        .createdDate(document.getCreatedDateTime())
        .build();
  }
}
