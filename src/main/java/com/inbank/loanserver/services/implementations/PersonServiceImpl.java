package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.exceptions.PersonNotFoundException;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.repositories.PersonRepository;
import com.inbank.loanserver.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementations of PersonService
 *
 * @author vinodjohn
 * @created 30.08.2024
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Override
    public Person createPerson(Person person) {
        person.setActive(true);
        return personRepository.save(person);
    }

    @Override
    public Person findPersonById(UUID id) throws PersonNotFoundException {
        Optional<Person> person = personRepository.findById(id);

        if (person.isEmpty()) {
            throw new PersonNotFoundException(id);
        }

        return person.get();
    }

    @Override
    public Person findPersonsByPersonalIdCode(String personalIdCode) throws PersonNotFoundException {
        Optional<Person> person = personRepository.findByPersonalIdCode(personalIdCode);

        if (person.isEmpty()) {
            throw new PersonNotFoundException(personalIdCode);
        }

        return person.get();
    }

    @Override
    public Page<Person> findAllPersons(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    @Override
    public Person updatePerson(Person person) throws PersonNotFoundException {
        if (findPersonById(person.getId()) != null) {
            return personRepository.save(person);
        }

        return null;
    }

    @Override
    public void deletePersonById(UUID id) throws PersonNotFoundException {
        Person person = findPersonById(id);
        person.setActive(false);
        personRepository.save(person);
    }

    @Override
    public void restorePersonById(UUID id) throws PersonNotFoundException {
        Person person = findPersonById(id);
        person.setActive(true);
        personRepository.save(person);
    }
}
