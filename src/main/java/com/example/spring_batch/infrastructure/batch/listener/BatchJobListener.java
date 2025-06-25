package com.example.spring_batch.infrastructure.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BatchJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("=== BATCH JOB STARTING ===");
        log.info("Job Name: {}", jobExecution.getJobInstance().getJobName());
        log.info("Job Parameters: {}", jobExecution.getJobParameters());
        log.info("Job Instance ID: {}", jobExecution.getJobInstance().getId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("=== BATCH JOB COMPLETED ===");
        log.info("Job Name: {}", jobExecution.getJobInstance().getJobName());
        log.info("Job Status: {}", jobExecution.getStatus());
        log.info("Exit Code: {}", jobExecution.getExitStatus().getExitCode());
        log.info("Start Time: {}", jobExecution.getStartTime());
        log.info("End Time: {}", jobExecution.getEndTime());
        
        // Log execution summary
        jobExecution.getStepExecutions().forEach(stepExecution -> {
            log.info("Step: {} - Read: {}, Written: {}, Skipped: {}", 
                    stepExecution.getStepName(),
                    stepExecution.getReadCount(),
                    stepExecution.getWriteCount(),
                    stepExecution.getSkipCount());
        });
        
        if (jobExecution.getStatus().isUnsuccessful()) {
            log.error("Job failed with exceptions: {}", jobExecution.getAllFailureExceptions());
        }
    }
} 