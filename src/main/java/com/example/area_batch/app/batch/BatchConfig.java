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
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.TargetAware;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.area_batch.app.model.entity.source.Area;
import com.example.area_batch.app.model.entity.source.Governorate;
import com.example.area_batch.app.model.entity.target.TargetArea;
import com.example.area_batch.app.model.entity.target.TargetBranch;
import com.example.area_batch.app.model.entity.target.TargetGovernorate;
import com.example.area_batch.app.repo.AreaRepo;
import com.example.area_batch.app.repo.BranchRepo;
import com.example.area_batch.app.repo.GovernorateRepo;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class BatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager ;
    @PersistenceUnit(unitName = "sourceEntityManagerFactory")
    private final EntityManagerFactory sourceEntityManagerFactory;
    @PersistenceUnit(unitName = "targetEntityManagerFactory")
    private final EntityManagerFactory targetEntityManagerFactory;
    private final AreaRepo areaRepo;
    private final GovernorateRepo governorateRepo;
    private final BranchRepo branchRepo;

    @Bean
    public Job syncJob(){
        return new JobBuilder("syncJob", jobRepository)
        .start(syncStep())
        .build();
    }


    private Step syncStep() {
       return new StepBuilder("syncStep", jobRepository)
            .<Area,TargetArea>chunk(10, transactionManager)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build();
    }

    private ItemReader<Area> reader(){
        JpaPagingItemReader<Area> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(sourceEntityManagerFactory);
        reader.setQueryString("select a from Area a");
        reader.setPageSize(100);
        return reader;
    }
    private ItemProcessor<Area,TargetArea> processor(){ 
        return sourceArea -> {
            if(areaRepo.findFirstByName(sourceArea.getName()).isPresent() )
                return null;

            TargetBranch targetBranch = branchRepo
                    .findFirstByName(sourceArea.getGovernorate().getBranch().getName())
                    .orElse(TargetBranch.builder()
                    .name(sourceArea.getGovernorate().getBranch().getName()).build());

            TargetGovernorate targetGovernorate = governorateRepo
                .findFirstByName(sourceArea.getGovernorate().getName())
                .orElse(TargetGovernorate.builder().name(sourceArea.getGovernorate().getName())
                .branch(targetBranch).build());

            return TargetArea.builder().governorate(targetGovernorate).name(sourceArea.getName()).build();
        };
    }
   

    private ItemWriter<TargetArea> writer(){
        JpaItemWriter<TargetArea> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(targetEntityManagerFactory);
        return writer;
    }

    
}
