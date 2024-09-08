package com.inbank.loanserver.repositories;

import com.inbank.loanserver.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository to handle Person related data queries
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person, UUID>, JpaRepository<Person, UUID> {
    Optional<Person> findByPersonalIdCode(String personalIdCode);
}
