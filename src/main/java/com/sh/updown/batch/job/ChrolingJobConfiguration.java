package com.sh.updown.batch.job;

import com.sh.updown.chroling.Interpark;
import com.sh.updown.chroling.Naver;
import com.sh.updown.batch.common.dto.ProductDto;
import com.sh.updown.batch.common.entity.Product;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

@Configuration
@EnableBatchProcessing
@Slf4j
public class ChrolingJobConfiguration {

    private final EntityManagerFactory entityManagerFactory;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final JobListener jobListener;
    private final Naver naver;
    private final Interpark interpark;

    public ChrolingJobConfiguration(EntityManagerFactory entityManagerFactory,
                                    JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager, JobListener jobListener,
                                    Naver naver,
                                    Interpark interpark) {
        this.entityManagerFactory = entityManagerFactory;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.jobListener = jobListener;
        this.naver = naver;
        this.interpark = interpark;
    }

    @Bean
    public Job chrolingJob() throws Exception {
        log.debug("chrolingJob 메소드가 호출됐습니다.");
        return new JobBuilder("chrolingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobListener)
                .start(naverFlow())
                .end()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        log.info("==== thread가 생성되었습니다. ====");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // 기본 스레드 수
        executor.setMaxPoolSize(10);  // 최대 스레드 수
        executor.setQueueCapacity(25); // 작업 대기 큐 용량
        executor.setThreadNamePrefix("Batch-Thread-"); // 스레드 이름 접두사
        executor.initialize();  // 초기화
        return executor;
    }

    @Bean
    public Flow interparkFlow() throws Exception {
        log.info("Interpark Flow가 시작됩니다.");
        return new FlowBuilder<Flow>("interparkFlow")
                .start(interparkChrolingStep())
                .build();
    }

    @Bean
    public Flow naverFlow() throws IOException {
        log.info("Naver Flow가 시작됩니다.");
        return new FlowBuilder<Flow>("naverFlow")
                .start(naverChrolingStep())
                .build();
    }

    @Bean
    public Step interparkChrolingStep() throws Exception {
        log.info("InterParkChrolling이 시작됩니다.");
        return new StepBuilder("interparkChrolingStep", jobRepository)
                .<ProductDto, Product>chunk(5, transactionManager)
                .reader(interParkReader())
                .processor(interParkProcessor())
                .writer(interparkWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<? extends ProductDto> interParkReader() throws Exception {
        log.info("==== InterPark ItemReader를 시작합니다. ====");
        return new InterParkReader(interpark);
    }

    @Bean
    @StepScope
    public ItemProcessor<ProductDto, Product> interParkProcessor() {
        log.info("==== InterPark ItemProcessor를 시작합니다. ====");
        return new InterParkeProcessor();
    }

    @Bean
    @StepScope
    public JpaItemWriter<Product> interparkWriter() {
        log.info("==== InterPark ItemWriter를 시작합니다. ====");
        return new JpaItemWriterBuilder<Product>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }

    @Bean
    public Step naverChrolingStep() throws IOException {
        log.info("NaverChroling이 시작됩니다.");
        return new StepBuilder("naverChrolingStep", jobRepository)
                .<ProductDto, Product>chunk(5, transactionManager)
                .reader(naverReader())
                .processor(naverProcessor())
                .writer(naverWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<? extends ProductDto> naverReader() throws IOException {
        log.info("==== Naver ItemReader를 시작합니다. ====");
        return new NaverItemReader(naver);
    }

    @Bean
    @StepScope
    public ItemProcessor<ProductDto, Product> naverProcessor() {
        log.info("==== Naver ItemProcessor를 시작합니다. ====");
        return new NaverItemProcessor();
    }

    @Bean
    @StepScope
    public JpaItemWriter<Product> naverWriter() {
        log.info("==== Naver ItemWriter를 시작합니다. ====");
        return new JpaItemWriterBuilder<Product>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }
}
