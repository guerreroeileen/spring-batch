package com.example.spring_batch.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "persons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", unique = true, length = 255)
    private String email;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }

    // Domain methods
    public String getFullName() {
        return String.format("%s %s", firstName, lastName).trim();
    }

    public boolean hasValidEmail() {
        return email != null && !email.trim().isEmpty();
    }

    public void generateEmail() {
        if (firstName != null && lastName != null) {
            this.email = String.format("%s.%s@example.com", 
                firstName.toLowerCase(), 
                lastName.toLowerCase());
        }
    }
} 