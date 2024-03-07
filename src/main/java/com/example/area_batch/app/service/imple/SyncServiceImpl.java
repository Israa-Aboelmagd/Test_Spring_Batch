package com.example.area_batch.app.service.imple;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

import com.example.area_batch.app.service.SyncService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SyncServiceImpl implements SyncService {
    private JobLauncher jobLauncher;
    private Job syncJob;

     
    @Override
    public void sync() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        long currentTime = System.currentTimeMillis();
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", currentTime)
                .toJobParameters();
        jobLauncher.run(syncJob,jobParameters);
    }
    
}
