package com.inbank.loanserver.controllers;

import com.inbank.loanserver.configurations.security.CustomUserDetails;
import com.inbank.loanserver.dtos.ObjectListDto;
import com.inbank.loanserver.exceptions.LoanApplicationNotFoundException;
import com.inbank.loanserver.models.LoanApplication;
import com.inbank.loanserver.services.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.inbank.loanserver.utils.Constants.Data.DEFAULT_ITEMS_PER_PAGE;
import static com.inbank.loanserver.utils.LoanUtils.getSortOfColumn;

/**
 * Controller to handle loan application related operations
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
@RestController
@RequestMapping("/loan-application")
public class LoanApplicationController {
    @Autowired
    private LoanApplicationService loanApplicationService;

    @GetMapping
    public ResponseEntity<ObjectListDto> getSortedLoanApplicationByPage(
            @RequestParam(name = "page", defaultValue = "0") int pageNum,
            @RequestParam(name = "items", defaultValue = DEFAULT_ITEMS_PER_PAGE) int totalItem,
            @RequestParam(name = "sort", defaultValue = "createdDate") String sort,
            @RequestParam(name = "order", defaultValue = "desc") String order) {
        Page<LoanApplication> loanApplicationPage =
                loanApplicationService.findAllLoanApplications(PageRequest.of(pageNum
                        , totalItem, getSortOfColumn(sort, order)));
        List<LoanApplication> loanApplicationList = loanApplicationPage.stream()
                .collect(Collectors.toList());
        ObjectListDto objectListDto = new ObjectListDto(loanApplicationList, pageNum,
                loanApplicationPage.getTotalElements());

        return ResponseEntity.ok(objectListDto);
    }

    @GetMapping("/personal")
    public ResponseEntity<ObjectListDto> getSortedLoanApplicationOfPersonByPage(
            @RequestParam(name = "page", defaultValue = "0") int pageNum,
            @RequestParam(name = "items", defaultValue = DEFAULT_ITEMS_PER_PAGE) int totalItem,
            @RequestParam(name = "sort", defaultValue = "createdDate") String sort,
            @RequestParam(name = "order", defaultValue = "desc") String order) throws LoanApplicationNotFoundException {
        CustomUserDetails customUserDetails =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<LoanApplication> loanApplicationPage =
                loanApplicationService.findLoanApplicationsByPerson(PageRequest.of(pageNum
                        , totalItem, getSortOfColumn(sort, order)), customUserDetails.getPerson());
        List<LoanApplication> loanApplicationList = loanApplicationPage.stream()
                .collect(Collectors.toList());
        ObjectListDto objectListDto = new ObjectListDto(loanApplicationList, pageNum,
                loanApplicationPage.getTotalElements());

        return ResponseEntity.ok(objectListDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLoanApplicationById(@PathVariable UUID id) throws LoanApplicationNotFoundException {
        LoanApplication loanApplication = loanApplicationService.findLoanApplicationById(id);
        return ResponseEntity.ok(loanApplication);
    }

    @PutMapping
    public ResponseEntity<?> updateLoanApplication(@RequestBody LoanApplication loanApplication) throws LoanApplicationNotFoundException {
        LoanApplication updatedLoanApplication = loanApplicationService.updateLoanApplication(loanApplication);
        return ResponseEntity.ok(updatedLoanApplication);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteLoanApplication(@PathVariable UUID id) throws LoanApplicationNotFoundException {
        loanApplicationService.deleteLoanApplicationById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restoreLoanApplication(@PathVariable UUID id) throws LoanApplicationNotFoundException {
        loanApplicationService.restoreLoanApplicationById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
