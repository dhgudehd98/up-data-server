package com.sh.updown.batch.job;


import com.sh.updown.chroling.Interpark;
import com.sh.updown.chroling.Naver;
import com.sh.updown.dto.ProductDto;
import com.sh.updown.entity.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;


@Configuration
@Slf4j
public class ChrolingJobConfiguration {
    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final Naver naver;
    private final Interpark interpark;

    @Autowired
    public ChrolingJobConfiguration(EntityManagerFactory entityManagerFactory, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, Naver naver, Interpark interpark) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.naver = naver;
        this.interpark = interpark;
    }

    @Bean
    public Job chrolingJob() throws Exception {
        log.debug("chrolingJob 메소드가 호출됐습니다.");
        return jobBuilderFactory.get("chrolingJob")
                .incrementer(new RunIdIncrementer())
                .start(interparkFlow())
//                .start(startStep1())  // 수정: startStep1을 일반적인 Step으로 변경
//                .split(taskExecutor())
//                .add(interparkFlow(), naverFlow())
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
    public Step startStep1() {
        return stepBuilderFactory.get("startStep1").tasklet((stepContribution, chunkContext) -> {
            System.out.println("========Chorlling 시작 =======");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
//    @JobScope
    public Flow interparkFlow() throws Exception {
        log.info("Interpark Flow가 시작됩니다.");
        return new FlowBuilder<Flow>("interparkFlow")
                .start(interparkChrolingStep())
                .build();
    }

    @Bean
//    @JobScope
    public Flow naverFlow() throws IOException {
        log.info("Naver Flow가 시작됩니다.");
        return new FlowBuilder<Flow>("naverFlow")
                .start(naverChrolingStep())
                .build();
    }

    @Bean
    public Step interparkChrolingStep() throws Exception {
        log.info("InterParkChrolling이 시작됩니다.");
        return stepBuilderFactory.get("interparkChrolingStep")
                .<ProductDto, ProductEntity>chunk(5)
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
    public ItemProcessor<ProductDto, ProductEntity> interParkProcessor() {
        log.info("==== InterPark ItemProcessor를 시작합니다. ====");
        return new InterParkeProcessor();
    }

    @Bean
    @StepScope
    public JpaItemWriter<ProductEntity> interparkWriter() {
        log.info("==== InterPark ItemWriter를 시작합니다. ====");
        return new JpaItemWriterBuilder<ProductEntity>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }

    @Bean
    public Step naverChrolingStep() throws IOException {
        log.info("NaverChroling이 시작됩니다.");
        return stepBuilderFactory.get("naverChrolingStep")
                .<ProductDto, ProductEntity>chunk(5)
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
    public ItemProcessor<ProductDto, ProductEntity> naverProcessor() {
        log.info("==== Naver ItemProcessor를 시작합니다. ====");
        return new NaverItemProcessor();
    }

    @Bean
    @StepScope
    public JpaItemWriter<ProductEntity> naverWriter() {
        log.info("==== Naver ItemWriter를 시작합니다. ====");
        return new JpaItemWriterBuilder<ProductEntity>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }
}
