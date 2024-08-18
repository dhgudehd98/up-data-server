//package com.sh.updown.batch.job;
//
//import com.sh.updown.chroling.Interpark;
//import com.sh.updown.chroling.Naver;
//import com.sh.updown.dto.ProductDto;
//import com.sh.updown.entity.ProductEntity;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.StepContribution;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.JobScope;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.scope.context.ChunkContext;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.database.JpaItemWriter;
//import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;
//import org.springframework.core.task.TaskExecutor;
//
//import javax.persistence.EntityManagerFactory;
//import java.io.IOException;
//
////@Configuration
//@Slf4j
//public class JobConfiguration {
//    private EntityManagerFactory entityManagerFactory;
//    private JobBuilderFactory jobBuilderFactory;
//    private StepBuilderFactory stepBuilderFactory;
//    private Naver naver;
//    private Interpark interpark;
//
//    @Autowired
//    public JobConfiguration(EntityManagerFactory entityManagerFactory, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, Naver naver, Interpark interpark) {
//        this.jobBuilderFactory = jobBuilderFactory;
//        this.stepBuilderFactory = stepBuilderFactory;
//        this.entityManagerFactory = entityManagerFactory;
//        this.naver = naver;
//        this.interpark = interpark;
////        this.jobLauncher = jobLauncher;
//
//    }
//
//    /**
//     * 순환 참조 발생
//     * 1. chrolingJob을 실행 시키기 위해서는 Bean으로 등록된 JobLauncher 필요함.
//     * 2. JobLauncher를 통해서 Job을 등록하기 위해서는 Bean으로 등록된 chorlingJob이 필요함
//     * -> 두개가 이렇게 서로 순환되는 방식이여서 에러 발생
//     *
//     * 이 문제점을 어떻게 해결 할 수 있을까 ?
//     * 1. @Lazy 어노테이션 사용
//     * 2. 필드 주입 , setter 주입
//     * -> 둘다 임시방편의 도구일뿐 지양해야되는 코드 방향이다.
//     *
//     * 그럼 설계할 때
//     */
//
//
//    @Bean
//    public Job chrolingJob() throws Exception {
//        log.debug("chrolingJob 메소드가 호출됐습니다.");
//        return jobBuilderFactory.get("chrolingJob")
//                .incrementer(new RunIdIncrementer())
//                .start(startstep1())
//                .next(interparkChrolingStep())
//                .next(naverChrolingStep())
//                .build();
//
//    }
//
//    @Bean
//    public TaskExecutor taskExecutor() {
//        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
//        taskExecutor.setConcurrencyLimit(10); // 동시 실행 스레드 수
//        return taskExecutor;
//    }
//
//    private Step startstep1() {
//        return stepBuilderFactory.get("startstep1")
//                .tasklet(new Tasklet() {
//                    @Override
//                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
//                        System.out.println("========================");
//                        System.out.println("Chroling 작업을 시작합니다 !");
//                        System.out.println("========================");
//                        return  RepeatStatus.FINISHED;
//                    }
//                }).build();
//    }
//
//    @Bean
//    @JobScope
//    public Step interparkChrolingStep() throws Exception {
//        log.info("InterParkChrolling이 시작됩니다.");
//        return stepBuilderFactory.get("interparkChrolingStep")
//                .<ProductDto, ProductEntity>chunk(5)
//                .reader(interParkReader())
//                .processor(interParkProcessor())
//                .writer(interparkWriter())
//                .build();
//
//    }
//
//    @Bean
//    @StepScope
//    public ItemReader<? extends ProductDto> interParkReader() throws Exception {
//        log.info("==== ItemReader를 시작합니다.====");
//        return new InterParkReader(interpark);
//    }
//
//    @Bean
//    @StepScope
//    public ItemProcessor<ProductDto,ProductEntity> interParkProcessor() {
//        return new InterParkeProcessor();
//    }
//
//
//    @Bean
//    @StepScope
//    public JpaItemWriter<ProductEntity> interparkWriter() {
//        return new JpaItemWriterBuilder<ProductEntity>()
//                .entityManagerFactory(entityManagerFactory)
//                .usePersist(true)
//                .build();
//    }
//
//
//
//    @Bean
//    @JobScope
//    public Step naverChrolingStep() throws IOException {
//        return stepBuilderFactory.get("naverChrolingStep")
//                .<ProductDto, ProductEntity>chunk(5)
//                .reader(naverReader())
//                .processor(naverProcessor())
//                .writer(naverWriter())
//                .build();
//    }
//
//
//    //네이버 패키지 여행 정보 읽기
//    @Bean
//    @StepScope
//    public ItemReader<? extends ProductDto> naverReader() throws IOException {
//        return new NaverItemReader(naver);
//    }
//
//    @Bean
//    @StepScope
//    public ItemProcessor<ProductDto, ProductEntity> naverProcessor() {
//        return new NaverItemProcessor();
//
//    }
//
//
//    @Bean
//    @StepScope
//    public JpaItemWriter<ProductEntity> naverWriter() {
//        return new JpaItemWriterBuilder<ProductEntity>()
//                .entityManagerFactory(entityManagerFactory)
//                .usePersist(true)
//                .build();
//    }
//}