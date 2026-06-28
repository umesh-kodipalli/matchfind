package com.example.personbackend.repository;

import com.example.personbackend.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Standard Spring Data JPA repository. JpaRepository already provides
 * save, findAll, findById, and delete — no custom methods are needed for
 * this API's requirements, so the interface is left empty on purpose.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
