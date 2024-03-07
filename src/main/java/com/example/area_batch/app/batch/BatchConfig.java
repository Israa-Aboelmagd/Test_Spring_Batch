package com.example.area_batch.app.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.TargetAware;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.area_batch.app.model.entity.source.Area;
import com.example.area_batch.app.model.entity.target.TargetArea;
import com.example.area_batch.app.model.entity.target.TargetBranch;
import com.example.area_batch.app.model.entity.target.TargetGovernorate;

import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class BatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager ;
     private final EntityManagerFactory sourceAreaFactory;
    private final EntityManagerFactory targetAreaFactory;

    @Bean
    public Job syncJob(){
        return new JobBuilder("syncJob", jobRepository)
        .start(syncStep())
        .build();
    }

    
    private Step syncStep() {
       return new StepBuilder("syncStep", jobRepository)
            .<Area,TargetArea>chunk(0, transactionManager)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build();
    }

    private ItemReader<Area> reader(){
        JpaPagingItemReader<Area> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(sourceAreaFactory);
        reader.setQueryString("select a from Area a");
        reader.setPageSize(100);
        return reader;
    }
    private ItemProcessor<Area,TargetArea> processor(){
        return sourceArea -> {
            TargetArea targetArea = new TargetArea();
            TargetGovernorate targetGovernorate = new TargetGovernorate();
            TargetBranch targetBranch = new TargetBranch();
            targetBranch.setName(sourceArea.getGovernorate().getBranch().getName()+"_suffix");
            targetGovernorate.setBranch(targetBranch);
            targetGovernorate.setName(sourceArea.getGovernorate().getName()+"_suffix");
            targetArea.setName(sourceArea.getName()+"_suffix");
            targetArea.setGovernorate(targetGovernorate);

            return targetArea;
        };
    }

    private ItemWriter<TargetArea> writer(){
        JpaItemWriter<TargetArea> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(targetAreaFactory);
        return writer;
    }

 
// public Step sampleStep(JobRepository jobRepository, 
// 		PlatformTransactionManager transactionManager) { 
// 	return new StepBuilder("sampleStep", jobRepository)
// 				.<String, String>chunk(10, transactionManager) 
// 				.reader(itemReader())
// 				.writer(itemWriter())
// 				.build();
// }
    
}
