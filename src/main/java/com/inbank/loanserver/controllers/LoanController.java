package com.inbank.loanserver.controllers;

import com.inbank.loanserver.configurations.security.CustomUserDetails;
import com.inbank.loanserver.dtos.LoanRequest;
import com.inbank.loanserver.dtos.LoanResponse;
import com.inbank.loanserver.exceptions.KeyValueStoreNotFoundException;
import com.inbank.loanserver.models.LoanApplication;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.services.LoanApplicationService;
import com.inbank.loanserver.services.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

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

    @PostMapping
    public ResponseEntity<LoanResponse> getLoanDecision(@Valid @RequestBody LoanRequest loanRequest)
            throws KeyValueStoreNotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails customUserDetails) {
            Person person = customUserDetails.getPerson();

            LoanApplication loanApplication = new LoanApplication();
            loanApplication.setPerson(person);
            loanApplication.setRequestAmount(loanRequest.loanAmount());
            loanApplication.setRequestPeriod(loanRequest.loanPeriod());

            LoanApplication newLoanApplication = loanApplicationService.createLoanApplication(loanApplication);
            LoanResponse loanResponse = loanService.getLoanDecision(newLoanApplication);
            return ResponseEntity.ok(loanResponse);
        } else {
            throw new IllegalStateException(MessageFormat.format("Unexpected principal type: {0}",
                    principal.getClass().getName()));
        }
    }
}
