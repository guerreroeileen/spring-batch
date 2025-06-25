package com.example.spring_batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing
public class SpringBatchApplication {

	public static void main(String[] args) {
		log.info("Starting Spring Batch Application...");
		SpringApplication.run(SpringBatchApplication.class, args);
		log.info("Spring Batch Application started successfully!");
	}

}
