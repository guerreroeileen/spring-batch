package com.example.spring_batch.infrastructure.batch.processor;

import com.example.spring_batch.domain.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person person) throws Exception {
        log.debug("Processing person: {} {}", person.getFirstName(), person.getLastName());

        // Validate input
        if (person == null) {
            log.warn("Received null person, skipping");
            return null;
        }

        if (Objects.isNull(person.getFirstName()) || Objects.isNull(person.getLastName())) {
            log.warn("Person with null firstName or lastName, skipping: {} {}", 
                    person.getFirstName(), person.getLastName());
            return null;
        }

        // Generate email
        person.generateEmail();
        
        log.debug("Generated email: {} for person: {} {}", 
                person.getEmail(), person.getFirstName(), person.getLastName());

        return person;
    }
} 