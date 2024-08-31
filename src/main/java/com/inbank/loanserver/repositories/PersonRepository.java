package com.inbank.loanserver.repositories;

import com.inbank.loanserver.models.Person;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository to handle Person related data queries
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
public interface PersonRepository extends PagingAndSortingRepository<Person, UUID>, ListCrudRepository<Person, UUID> {
    Optional<Person> findByPersonalIdCode(String personalIdCode);
}
