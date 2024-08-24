package com.up.down.batch.config;

import com.up.down.batch.indexing.job.Indexer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@Slf4j
public class IndexingJobConfig {

    private final JobRepository jobRepo;
    private final PlatformTransactionManager transactionManager;
    private final Indexer indexer;

    public IndexingJobConfig(JobRepository jobRepo,
                             PlatformTransactionManager transactionManager,
                             Indexer indexer
    ) {
        this.jobRepo = jobRepo;
        this.transactionManager = transactionManager;
        this.indexer = indexer;
    }

    @Bean
    public Job indexingJob() {
        return new JobBuilder("indexingJob", jobRepo)
                .incrementer(new RunIdIncrementer())
                .start(storeProductGroupStep())
                .next(indexProductGroupStep())
                .build();
    }

    @Bean
    public Step storeProductGroupStep() {
        return new StepBuilder("storeProductGroupStep", jobRepo)
                .tasklet((contribution, chunkContext) -> {
                    indexer.storeInDatabase();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step indexProductGroupStep() {
        return new StepBuilder("indexProductGroupStep", jobRepo)
                .tasklet((contribution, chunkContext) -> {
                    indexer.indexInElasticsearch();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}