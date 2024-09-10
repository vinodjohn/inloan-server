package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.PersonNotFoundException;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.repositories.PersonRepository;
import com.inbank.loanserver.services.implementations.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Tests for PersonService
 *
 * @author vinodjohn
 * @created 10.09.2024
 */
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    private Person person;
    private UUID personId;
    private String personalIdCode;

    @BeforeEach
    void setUp() {
        personId = UUID.randomUUID();
        personalIdCode = "1234567890";
        person = createPerson(personId, personalIdCode);
    }

    private Person createPerson(UUID id, String personalIdCode) {
        Person person = new Person();
        person.setId(id);
        person.setPersonalIdCode(personalIdCode);
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setPassword("password");
        person.setActive(true);
        return person;
    }

    @Test
    void testFindPersonByIdWhenValidIdThenPersonIsReturned() throws PersonNotFoundException {
        when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        Person foundPerson = personService.findPersonById(personId);
        assertEquals(person, foundPerson);
        verify(personRepository, times(1)).findById(personId);
    }

    @Test
    void testFindPersonByPersonalIdCodeWhenValidCodeThenPersonIsReturned() throws PersonNotFoundException {
        when(personRepository.findByPersonalIdCode(personalIdCode)).thenReturn(Optional.of(person));
        Person foundPerson = personService.findPersonByPersonalIdCode(personalIdCode);
        assertEquals(person, foundPerson);
        verify(personRepository, times(1)).findByPersonalIdCode(personalIdCode);
    }

    @Test
    void testFindActivePersonByPersonalIdCodeWhenValidCodeThenPersonIsReturned() throws PersonNotFoundException {
        when(personRepository.findByPersonalIdCode(personalIdCode)).thenReturn(Optional.of(person));
        Person foundPerson = personService.findActivePersonByPersonalIdCode(personalIdCode);
        assertEquals(person, foundPerson);
        verify(personRepository, times(1)).findByPersonalIdCode(personalIdCode);
    }

    @Test
    void testFindActivePersonByPersonalIdCodeWhenInactiveThenExceptionIsThrown() {
        person.setActive(false);
        when(personRepository.findByPersonalIdCode(personalIdCode)).thenReturn(Optional.of(person));
        assertThrows(PersonNotFoundException.class,
                () -> personService.findActivePersonByPersonalIdCode(personalIdCode));
        verify(personRepository, times(1)).findByPersonalIdCode(personalIdCode);
    }

    @Test
    void testFindAllPersonsWhenValidPageableThenPageIsReturned() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Person> personPage = new PageImpl<>(List.of(person));
        when(personRepository.findAll(pageable)).thenReturn(personPage);
        Page<Person> foundPersons = personService.findAllPersons(pageable);
        assertEquals(personPage, foundPersons);
        verify(personRepository, times(1)).findAll(pageable);
    }
}