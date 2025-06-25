package com.example.spring_batch.application.service;

import com.example.spring_batch.domain.model.Person;
import com.example.spring_batch.domain.repository.PersonRepository;
import com.example.spring_batch.interfaces.dto.PersonDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    /**
     * Find all persons with pagination
     */
    public Page<PersonDto> findAllPersons(Pageable pageable) {
        log.debug("Finding all persons with pagination: {}", pageable);
        return personRepository.findAll(pageable)
                .map(this::toDto);
    }

    /**
     * Find person by ID
     */
    public Optional<PersonDto> findPersonById(UUID id) {
        log.debug("Finding person by ID: {}", id);
        return personRepository.findById(id)
                .map(this::toDto);
    }

    /**
     * Find person by email
     */
    public Optional<PersonDto> findPersonByEmail(String email) {
        log.debug("Finding person by email: {}", email);
        return personRepository.findByEmail(email)
                .map(this::toDto);
    }

    /**
     * Search persons by name
     */
    public Page<PersonDto> searchPersonsByName(String name, Pageable pageable) {
        log.debug("Searching persons by name: {}", name);
        return personRepository.searchByName(name, pageable)
                .map(this::toDto);
    }

    /**
     * Get statistics
     */
    public PersonStatistics getStatistics() {
        log.debug("Getting person statistics");
        long totalPersons = personRepository.count();
        long personsWithExampleDomain = personRepository.countByEmailDomain("@example.com");
        
        return PersonStatistics.builder()
                .totalPersons(totalPersons)
                .personsWithExampleDomain(personsWithExampleDomain)
                .build();
    }

    /**
     * Save person
     */
    @Transactional
    public PersonDto savePerson(Person person) {
        log.debug("Saving person: {}", person.getFullName());
        Person savedPerson = personRepository.save(person);
        return toDto(savedPerson);
    }

    /**
     * Save multiple persons
     */
    @Transactional
    public List<PersonDto> savePersons(List<Person> persons) {
        log.debug("Saving {} persons", persons.size());
        List<Person> savedPersons = personRepository.saveAll(persons);
        return savedPersons.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert Person to PersonDto
     */
    private PersonDto toDto(Person person) {
        return PersonDto.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .email(person.getEmail())
                .fullName(person.getFullName())
                .createdAt(person.getCreatedAt())
                .updatedAt(person.getUpdatedAt())
                .build();
    }

    /**
     * Statistics DTO
     */
    @lombok.Data
    @lombok.Builder
    public static class PersonStatistics {
        private long totalPersons;
        private long personsWithExampleDomain;
    }
} 