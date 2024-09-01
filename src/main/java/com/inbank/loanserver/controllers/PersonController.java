package com.inbank.loanserver.controllers;

import com.inbank.loanserver.dtos.ObjectListDto;
import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.exceptions.PersonNotFoundException;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.services.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.inbank.loanserver.utils.Constants.Data.DEFAULT_ITEMS_PER_PAGE;
import static com.inbank.loanserver.utils.LoanUtils.getSortOfColumn;

/**
 * Controller to handle person related operations
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping
    public ResponseEntity<ObjectListDto> getSortedPersonByPage(
            @RequestParam(name = "page", defaultValue = "0") int pageNum,
            @RequestParam(name = "items", defaultValue = DEFAULT_ITEMS_PER_PAGE) int totalItem,
            @RequestParam(name = "sort", defaultValue = "createdDate") String sort,
            @RequestParam(name = "order", defaultValue = "desc") String order) {
        Page<Person> personPage = personService.findAllPersons(PageRequest.of(pageNum, totalItem,
                getSortOfColumn(sort, order)));
        List<Person> personList = personPage.stream()
                .collect(Collectors.toList());
        ObjectListDto objectListDto = new ObjectListDto(personList, pageNum, personPage.getTotalElements());
        return ResponseEntity.ok(objectListDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable UUID id) throws PersonNotFoundException {
        Person person = personService.findPersonById(id);
        return ResponseEntity.ok(person);
    }

    @PostMapping
    public ResponseEntity<?> createPerson(@Valid @RequestBody Person person) throws CreditModifierNotFoundException {
        Person newPerson = personService.createPerson(person);
        return new ResponseEntity<>(newPerson, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updatePerson(@Valid @RequestBody Person person) throws PersonNotFoundException {
        Person updatedPerson = personService.updatePerson(person);
        return ResponseEntity.ok(updatedPerson);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable UUID id) throws PersonNotFoundException {
        personService.deletePersonById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restorePerson(@PathVariable UUID id) throws PersonNotFoundException {
        personService.restorePersonById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
