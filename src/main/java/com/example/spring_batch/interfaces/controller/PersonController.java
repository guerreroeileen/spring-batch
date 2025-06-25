package com.example.spring_batch.interfaces.controller;

import com.example.spring_batch.application.service.PersonService;
import com.example.spring_batch.interfaces.dto.ApiResponse;
import com.example.spring_batch.interfaces.dto.PersonDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    /**
     * Get all persons with pagination
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PersonDto>>> getAllPersons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.info("Getting all persons - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PersonDto> persons = personService.findAllPersons(pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Persons retrieved successfully", persons));
    }

    /**
     * Get person by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonDto>> getPersonById(@PathVariable UUID id) {
        log.info("Getting person by ID: {}", id);

        Optional<PersonDto> person = personService.findPersonById(id);
        
        return person.map(p -> ResponseEntity.ok(ApiResponse.success("Person found", p)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get person by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<PersonDto>> getPersonByEmail(@PathVariable String email) {
        log.info("Getting person by email: {}", email);

        Optional<PersonDto> person = personService.findPersonByEmail(email);
        
        return person.map(p -> ResponseEntity.ok(ApiResponse.success("Person found", p)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Search persons by name
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<PersonDto>>> searchPersons(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("Searching persons by name: {}", name);

        Pageable pageable = PageRequest.of(page, size);
        Page<PersonDto> persons = personService.searchPersonsByName(name, pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Search completed", persons));
    }

    /**
     * Get person statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<PersonService.PersonStatistics>> getStatistics() {
        log.info("Getting person statistics");

        PersonService.PersonStatistics statistics = personService.getStatistics();
        
        return ResponseEntity.ok(ApiResponse.success("Statistics retrieved", statistics));
    }
} 