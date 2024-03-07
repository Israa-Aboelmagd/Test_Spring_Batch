package com.example.area_batch.app.controller;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.area_batch.app.service.SyncService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SyncController {

    private final SyncService syncService;
    @GetMapping("/sync")
    public String sync() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
        syncService.sync();
        return "sync method";
    }
    
}
