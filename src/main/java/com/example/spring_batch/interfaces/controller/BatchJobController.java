package com.example.spring_batch.interfaces.controller;

import com.example.spring_batch.application.service.BatchJobService;
import com.example.spring_batch.interfaces.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class BatchJobController {

    private final BatchJobService batchJobService;

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        log.info("Health check endpoint called");
        return ResponseEntity.ok(ApiResponse.success("Spring Batch Application is running!"));
    }

    /**
     * Get job information
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<BatchJobService.JobInfo>> getJobInfo() {
        log.info("Job info endpoint called");
        
        BatchJobService.JobInfo jobInfo = batchJobService.getJobInfo();
        return ResponseEntity.ok(ApiResponse.success("Job information retrieved", jobInfo));
    }

    /**
     * Start the import persons job
     */
    @PostMapping("/import-persons")
    public ResponseEntity<ApiResponse<BatchJobService.JobExecutionResult>> startImportPersonsJob() {
        log.info("Starting import persons job");
        
        try {
            BatchJobService.JobExecutionResult result = batchJobService.startImportPersonsJob();
            return ResponseEntity.ok(ApiResponse.success("Job started successfully", result));
            
        } catch (Exception e) {
            log.error("Failed to start job", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to start job: " + e.getMessage()));
        }
    }
} 