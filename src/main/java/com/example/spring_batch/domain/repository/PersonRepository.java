package com.example.spring_batch.domain.repository;

import com.example.spring_batch.domain.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    /**
     * Find person by email
     */
    Optional<Person> findByEmail(String email);

    /**
     * Find persons by first name
     */
    List<Person> findByFirstNameIgnoreCase(String firstName);

    /**
     * Find persons by last name
     */
    List<Person> findByLastNameIgnoreCase(String lastName);

    /**
     * Find persons by first name and last name
     */
    List<Person> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    /**
     * Search persons by name (first name or last name)
     */
    @Query("SELECT p FROM Person p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Person> searchByName(@Param("name") String name, Pageable pageable);

    /**
     * Count persons by email domain
     */
    @Query("SELECT COUNT(p) FROM Person p WHERE p.email LIKE %:domain")
    long countByEmailDomain(@Param("domain") String domain);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
} 