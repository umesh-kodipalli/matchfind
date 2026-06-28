package com.example.personbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * JPA entity backing the "person_names" table.
 *
 * Kept as a plain persistence model (no validation annotations here) —
 * input validation belongs on
 * {@link com.example.personbackend.dto.PersonRequest},
 * which is what the controller actually validates.
 */
@Entity
@Table(name = "person_names")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "person1", nullable = false, length = 255)
    private String person1;

    @Column(name = "person2", nullable = false, length = 255)
    private String person2;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "percentage", nullable = false, length = 255)
    private Integer percentage;

    /**
     * Stamps createdAt automatically right before the row is first inserted,
     * so callers never have to set it themselves.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
