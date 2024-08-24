package com.up.down.batch.crawling.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobListener implements JobExecutionListener {

    private final JobLauncher jobLauncher;  // JobLauncher를 주입받습니다.
    private final Job indexingJob;  // 실행할 productGroupIndexingJob을 주입받습니다.

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus().isUnsuccessful()) {
            log.error("chrolingJob 실패로 인해 productGroupIndexingJob이 실행되지 않았습니다.");
            return;
        }

        log.info("chrolingJob이 완료되었습니다. 이제 productGroupIndexingJob을 실행합니다.");

        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(indexingJob, jobParameters);
        } catch (Exception e) {
            log.error("productGroupIndexingJob 실행 중 오류 발생", e);
        }
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("chrolingJob이 시작됩니다.");
    }
}
