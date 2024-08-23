package com.zb.wiki.config;

import com.zb.wiki.domain.Approval;
import com.zb.wiki.domain.Document;
import com.zb.wiki.elasticsearch.DocumentDocument;
import com.zb.wiki.repository.ApprovalRepository;
import com.zb.wiki.repository.DocumentRepository;
import com.zb.wiki.repository.DocumentSearchRepository;
import com.zb.wiki.type.DocumentStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class DocumentApprovalBatchConfig extends DefaultBatchConfiguration {

  private static final double APPROVAL_PERC = 0.7;
  private static final int APPROVAL_COUNT = 10;

  private final ApprovalRepository approvalRepository;
  private final DocumentRepository documentRepository;
  private final DocumentSearchRepository documentSearchRepository;
  private static final int CHUNK_SIZE = 10;

  @Bean
  public Job job(final JobRepository jobRepository, final Step updateDocumentStatusJob) {
    return new JobBuilder("documentUpdate", jobRepository)
        .start(updateDocumentStatusJob)
        .build();
  }

  @Bean
  public Step step(final JobRepository jobRepository,
      final PlatformTransactionManager transactionManager) {
    return new StepBuilder("step", jobRepository)
        .<Document, Document>chunk(CHUNK_SIZE, transactionManager)
        .reader(documentReader())
        .writer(documentWriter())
        .build();
  }

  /**
   * Pending 상태이면서 생성된지 7일이 지난 모든 문서 조회
   * @return Pending 상태의 문서 (10개 단위)
   */
  private ListItemReader<Document> documentReader() {
    return new ListItemReader<>(documentRepository.findByDocumentStatusAndCreatedDateBefore(
        DocumentStatus.PENDING, LocalDateTime.now().minusDays(7)));
  }

  @Bean
  public ItemWriter<Document> documentWriter() {
    return this::updateDocumentStateAndSaveToElasticSearch;
  }

  /**
   * 문서의 승인 미승인 여부 결정
   * 1. 승인/미승인 총 개수가 10개 이상인지
   * 2. 승인의 비율이 70%가 넘는지 확인 후 승인/미승인 여부 결정
   * @param objects
   */
  protected void updateDocumentStateAndSaveToElasticSearch(final Chunk<? extends Document> objects) {
    objects.getItems().forEach(item -> {
      List<Approval> approvalList = approvalRepository.findByDocument(item);
      long approvalCount = approvalList.stream().filter(Approval::isApproved).count();
      boolean shouldApprove =
          approvalList.size() >= APPROVAL_COUNT
              && (double) approvalCount / approvalList.size() >= APPROVAL_PERC;

      if (shouldApprove) {
        documentRepository.updateDocumentStatusById(item.getId(), DocumentStatus.APPROVED);
        documentSearchRepository.save(DocumentDocument.from(item));
      } else {
        documentRepository.updateDocumentStatusById(item.getId(), DocumentStatus.REJECTED);
      }
    });
  }
}
