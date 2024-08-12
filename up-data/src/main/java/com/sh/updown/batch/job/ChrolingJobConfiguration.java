package com.sh.updown.batch.job;//package com.sh.updown.batch.job;
//
//
//import com.sh.updown.chroling.Interpark;
//import com.sh.updown.chroling.Naver;
//import com.sh.updown.dto.ProductDto;
//import com.sh.updown.entity.ProductEntity;
//import org.modelmapper.ModelMapper;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.database.JpaItemWriter;
//import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.persistence.EntityManagerFactory;
//import java.io.IOException;
//
//@Configuration
//public class ChrolingJobConfiguration {
//
//    private EntityManagerFactory entityManagerFactory;
//    private JobBuilderFactory jobBuilderFactory;
//    private StepBuilderFactory stepBuilderFactory;
//    private Naver naver;
//    private Interpark interpark;
//
//    @Autowired
//    public ChrolingJobConfiguration(EntityManagerFactory entityManagerFactory, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,Naver naver, Interpark interpark) {
//        this.jobBuilderFactory = jobBuilderFactory;
//        this.stepBuilderFactory = stepBuilderFactory;
//        this.entityManagerFactory = entityManagerFactory;
//        this.naver = naver;
//        this.interpark = interpark;
//    }
//
//    @Bean
//    public Job chrollingJob() throws Exception {
//        return jobBuilderFactory.get("chrollingJob")
//                .incrementer(new RunIdIncrementer())
//                .start(chrolingStep())
//                .build();
//    }
//
////    @Bean
////    public Step chrolingStep() throws IOException {
////        return stepBuilderFactory.get("chrolingStep")
////                .<ProductDto, ProductEntity>chunk(3)
////                .reader(naverReader())
////                .processor(naverProcessor())
////                .writer(naverWriter())
////                .build();
////
////    }
//
//    @Bean
//    public Step chrolingStep() throws Exception {
//        return stepBuilderFactory.get("chrolingStep")
//                .<ProductDto, ProductEntity>chunk(3)
//                .reader(interParkReader())
//                .processor(interParkProcessor())
//                .writer(interparkWriter())
//                .build();
//
//    }
//
//    private ItemReader<? extends ProductDto> interParkReader() throws Exception {
//        return new InterParkReader(interpark);
//    }
//
//    private ItemProcessor<ProductDto,ProductEntity> interParkProcessor() {
//        return new InterParkeProcessor();
//    }
//
//    //    private ItemWriter<? super ProductEntity> interParkWriter() {
////        return new InterParkWriter();
////    }
//
//    @Bean
//    public JpaItemWriter<ProductEntity> interparkWriter() {
//        return new JpaItemWriterBuilder<ProductEntity>()
//                .entityManagerFactory(entityManagerFactory)
//                .usePersist(true)
//                .build();
//    }
//
//    //네이버 패키지 여행 정보 읽기
//    @Bean
//    public ItemReader<? extends ProductDto> naverReader() throws IOException {
//        return new NaverItemReader(naver);
//    }
//
//    @Bean
//    public ItemProcessor<ProductDto, ProductEntity> naverProcessor() {
//        return new NaverItemProcessor();
//
//    }
//
//
//    @Bean
//    public JpaItemWriter<ProductEntity> naverWriter() {
//        return new JpaItemWriterBuilder<ProductEntity>()
//                .entityManagerFactory(entityManagerFactory)
//                .usePersist(true)
//                .build();
//    }
//
//    //인터파크 정보 읽기
//
//
//    @Bean
//    public ModelMapper modelMapper() {
//        return new ModelMapper();
//    }
//
//
//
//
//
//
//
//}