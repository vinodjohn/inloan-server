package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.exceptions.PersonNotFoundException;
import com.inbank.loanserver.models.CreditModifier;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.repositories.PersonRepository;
import com.inbank.loanserver.services.CreditModifierService;
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

    @Autowired
    private CreditModifierService creditModifierService;

    @Override
    public Person createPerson(Person person) throws CreditModifierNotFoundException {
        if (person.getCreditModifier() == null) {
            CreditModifier randomCreditModifier = creditModifierService.findRandomCreditModifier();
            person.setCreditModifier(randomCreditModifier);
        }

        person.setActive(true);
        return personRepository.saveAndFlush(person);
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
    public Person findPersonByPersonalIdCode(String personalIdCode) throws PersonNotFoundException {
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
            return personRepository.saveAndFlush(person);
        }

        return null;
    }

    @Override
    public void deletePersonById(UUID id) throws PersonNotFoundException {
        Person person = findPersonById(id);
        person.setActive(false);
        personRepository.saveAndFlush(person);
    }

    @Override
    public void restorePersonById(UUID id) throws PersonNotFoundException {
        Person person = findPersonById(id);
        person.setActive(true);
        personRepository.saveAndFlush(person);
    }
}
