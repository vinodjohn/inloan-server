package com.inbank.loanserver.controllers;

import com.inbank.loanserver.dtos.LoanRequest;
import com.inbank.loanserver.dtos.LoanResponse;
import com.inbank.loanserver.exceptions.KeyValueStoreNotFoundException;
import com.inbank.loanserver.exceptions.PersonNotFoundException;
import com.inbank.loanserver.models.LoanApplication;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.services.LoanApplicationService;
import com.inbank.loanserver.services.LoanService;
import com.inbank.loanserver.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to handle loan decision related operations
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
@RestController
@RequestMapping("/loan")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanApplicationService loanApplicationService;

    @Autowired
    private PersonService personService;

    @PostMapping
    public ResponseEntity<LoanResponse> getLoanDecision(@RequestBody LoanRequest loanRequest)
            throws PersonNotFoundException, KeyValueStoreNotFoundException {
        Person person = personService.findPersonByPersonalIdCode(loanRequest.personalIdCode());

        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setPerson(person);
        loanApplication.setRequestAmount(loanRequest.loanAmount());
        loanApplication.setRequestPeriod(loanRequest.loanPeriod());

        LoanApplication newLoanApplication = loanApplicationService.createLoanApplication(loanApplication);
        LoanResponse loanResponse = loanService.getLoanDecision(newLoanApplication);

        return ResponseEntity.ok(loanResponse);
    }
}
