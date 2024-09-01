package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.PersonNotFoundException;
import com.inbank.loanserver.models.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service to handle Person related operations
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
public interface PersonService {
    /**
     * To create a new person
     *
     * @param person Person
     * @return Person
     */
    Person createPerson(Person person);

    /**
     * To find a Person by ID
     *
     * @param id ID of a Person
     * @return Person
     */
    Person findPersonById(UUID id) throws PersonNotFoundException;

    /**
     * To find person by personalID code
     *
     * @param personalIdCode personalId code of a person
     * @return Page of person
     */
    Person findPersonByPersonalIdCode(String personalIdCode) throws PersonNotFoundException;

    /**
     * To find all persons
     *
     * @param pageable Pageable of Persons
     * @return page of person
     */
    Page<Person> findAllPersons(Pageable pageable);

    /**
     * To update an existing person
     *
     * @param person Person
     * @return Person
     */
    Person updatePerson(Person person) throws PersonNotFoundException;

    /**
     * To delete a person by ID
     *
     * @param id Person ID
     */
    void deletePersonById(UUID id) throws PersonNotFoundException;

    /**
     * To restore a person by ID
     *
     * @param id Person ID
     */
    void restorePersonById(UUID id) throws PersonNotFoundException;
}
