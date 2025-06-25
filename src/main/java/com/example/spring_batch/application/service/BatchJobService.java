package com.example.spring_batch.application.service;

import com.example.spring_batch.shared.exception.BatchJobException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchJobService {

    private final JobLauncher jobLauncher;
    private final Job importPersonJob;

    /**
     * Start the import persons job
     */
    public JobExecutionResult startImportPersonsJob() {
        log.info("Starting import persons job");
        
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .addString("startTime", LocalDateTime.now().toString())
                    .toJobParameters();

            log.debug("Job parameters: {}", jobParameters);
            var jobExecution = jobLauncher.run(importPersonJob, jobParameters);
            
            return JobExecutionResult.builder()
                    .jobExecutionId(jobExecution.getId())
                    .jobInstanceId(jobExecution.getJobInstance().getId())
                    .status(jobExecution.getStatus().name())
                    .startTime(jobExecution.getStartTime())
                    .endTime(jobExecution.getEndTime())
                    .build();

        } catch (JobExecutionAlreadyRunningException e) {
            log.error("Job is already running", e);
            throw new BatchJobException("Job is already running", e);
            
        } catch (JobRestartException e) {
            log.error("Job restart failed", e);
            throw new BatchJobException("Job restart failed", e);
            
        } catch (JobInstanceAlreadyCompleteException e) {
            log.error("Job instance already complete", e);
            throw new BatchJobException("Job instance already complete", e);
            
        } catch (JobParametersInvalidException e) {
            log.error("Invalid job parameters", e);
            throw new BatchJobException("Invalid job parameters", e);
        }
    }

    /**
     * Get job information
     */
    public JobInfo getJobInfo() {
        return JobInfo.builder()
                .jobName(importPersonJob.getName())
                .jobDescription("Import persons from CSV to database")
                .build();
    }

    /**
     * Job execution result DTO
     */
    @lombok.Data
    @lombok.Builder
    public static class JobExecutionResult {
        private Long jobExecutionId;
        private Long jobInstanceId;
        private String status;
        private java.time.LocalDateTime startTime;
        private java.time.LocalDateTime endTime;
    }

    /**
     * Job information DTO
     */
    @lombok.Data
    @lombok.Builder
    public static class JobInfo {
        private String jobName;
        private String jobDescription;
    }
} 