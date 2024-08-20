package com.zb.wiki.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class DocumentApprovalBatchSecheduler {

  private final JobLauncher jobLauncher;
  private final Job updateDocumentStatusJob;

  /**
   * 문서 상태 업데이트 스케쥴러
   * 매 새벽 1시마다 스케쥴링
   * @throws Exception
   */
  @Scheduled(cron = "0 1 * * * *")
  public void runBatch() throws Exception {
    log.info("document Update Scheduler run");
    JobParameters jobParameters = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();
    jobLauncher.run(updateDocumentStatusJob, jobParameters);
  }
}
