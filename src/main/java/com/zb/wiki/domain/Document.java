package com.zb.wiki.domain;

import com.zb.wiki.type.DocumentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Document {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;

  @Column(unique = true)
  @NotNull
  private String title;
  @NotNull
  private String context;
  @Column(nullable = true)
  private String tag;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "author_id")
  private Member createdBy;

  @NotNull
  @Enumerated(EnumType.STRING)
  private DocumentStatus documentStatus;

  @CreatedDate
  private LocalDateTime createdDateTime;
  @LastModifiedDate
  private LocalDateTime modifiedDateTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "modified_id")
  private Member modifiedBy;

  public void update(String context, String tags, Member modifiedBy){
    this.context = context;
    this.tag = tags;
    this.modifiedBy = modifiedBy;
  }
}
