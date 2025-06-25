package com.example.spring_batch.infrastructure.batch.config;

import com.example.spring_batch.domain.model.Person;
import com.example.spring_batch.infrastructure.batch.listener.BatchJobListener;
import com.example.spring_batch.infrastructure.batch.processor.PersonItemProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final BatchJobListener batchJobListener;
    private final PersonItemProcessor personItemProcessor;

    @Bean
    public FlatFileItemReader<Person> personItemReader() {
        log.info("Configuring FlatFileItemReader for persons.csv");
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("input/persons.csv"))
                .delimited()
                .names("firstName", "lastName")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Person.class);
                }})
                .linesToSkip(1) // Skip header
                .build();
    }

    @Bean
    public JpaItemWriter<Person> personItemWriter(jakarta.persistence.EntityManagerFactory emf) {
        log.info("Configuring JpaItemWriter with EntityManagerFactory");
        return new JpaItemWriterBuilder<Person>()
                .entityManagerFactory(emf)
                .build();
    }

    @Bean
    public Job importPersonsJob(JobRepository jobRepository, Step importPersonsStep) {
        log.info("Configuring importPersonsJob");
        return new JobBuilder("importPersonsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(batchJobListener)
                .flow(importPersonsStep)
                .end()
                .build();
    }

    @Bean
    public Step importPersonsStep(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager,
                                 FlatFileItemReader<Person> personItemReader,
                                 JpaItemWriter<Person> personItemWriter) {
        log.info("Configuring importPersonsStep with chunk size 10");
        return new StepBuilder("importPersonsStep", jobRepository)
                .<Person, Person>chunk(10, transactionManager)
                .reader(personItemReader)
                .processor(personItemProcessor)
                .writer(personItemWriter)
                .faultTolerant()
                .skipLimit(10)
                .skip(Exception.class)
                .build();
    }
} 