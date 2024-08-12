package com.sh.updown.batch.job;




import com.sh.updown.chroling.Interpark;
import com.sh.updown.chroling.Naver;
import com.sh.updown.dto.ProductDto;
import com.sh.updown.entity.ProductEntity;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;

@Configuration
public class ChrolingJobConfiguration1 {

    private EntityManagerFactory entityManagerFactory;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private Naver naver;
    private Interpark interpark;

    @Autowired
    public ChrolingJobConfiguration1(EntityManagerFactory entityManagerFactory, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,Naver naver, Interpark interpark) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.naver = naver;
        this.interpark = interpark;
    }

    @Bean
    public Job chrollingJob() throws Exception {
        return jobBuilderFactory.get("chrollingJob")
                .incrementer(new RunIdIncrementer())
                .start(startstep1())
                .split(taskExecutor()).add(interparkFlow(), naverFlow())
                .end()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // 기본 스레드 수
        executor.setMaxPoolSize(10);  // 최대 스레드 수
        executor.setQueueCapacity(25); // 작업 대기 큐 용량
        executor.setThreadNamePrefix("Batch-Thread-"); // 스레드 이름 접두사
        executor.initialize();  // 초기화
        return executor;
    }

    private Step startstep1() {
        return stepBuilderFactory.get("startstep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("Chroling 작업을 시작합니다 !");
                        return  RepeatStatus.FINISHED;
                    }
                }).build();
    }

    private Flow interparkFlow() throws Exception {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("interparkFlow");
        flowBuilder.start(interparkChrolingStep()).end();
        return flowBuilder.build();
    }

    @Bean
    public Step interparkChrolingStep() throws Exception {
        return stepBuilderFactory.get("interparkChrolingStep")
                .<ProductDto, ProductEntity>chunk(3)
                .reader(interParkReader())
                .processor(interParkProcessor())
                .writer(interparkWriter())
                .build();

    }

    private ItemReader<? extends ProductDto> interParkReader() throws Exception {
        return new InterParkReader(interpark);
    }

    private ItemProcessor<ProductDto,ProductEntity> interParkProcessor() {
        return new InterParkeProcessor();
    }


    @Bean
    public JpaItemWriter<ProductEntity> interparkWriter() {
        return new JpaItemWriterBuilder<ProductEntity>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }

    private Flow naverFlow() throws IOException {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("naverFlow");
        flowBuilder.start(naverChrolingStep()).end();
        return flowBuilder.build();

    }

    @Bean
    public Step naverChrolingStep() throws IOException {
        return stepBuilderFactory.get("naverChrolingStep")
                .<ProductDto, ProductEntity>chunk(3)
                .reader(naverReader())
                .processor(naverProcessor())
                .writer(naverWriter())
                .build();
    }


    //네이버 패키지 여행 정보 읽기
    @Bean
    public ItemReader<? extends ProductDto> naverReader() throws IOException {
        return new NaverItemReader(naver);
    }

    @Bean
    public ItemProcessor<ProductDto, ProductEntity> naverProcessor() {
        return new NaverItemProcessor();

    }


    @Bean
    public JpaItemWriter<ProductEntity> naverWriter() {
        return new JpaItemWriterBuilder<ProductEntity>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }

    //인터파크 정보 읽기



}