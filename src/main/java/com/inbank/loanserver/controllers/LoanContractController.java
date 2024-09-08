package com.inbank.loanserver.controllers;

import com.inbank.loanserver.configurations.security.CustomUserDetails;
import com.inbank.loanserver.dtos.LoanContractRequest;
import com.inbank.loanserver.dtos.ObjectListDto;
import com.inbank.loanserver.exceptions.LoanContractNotFoundException;
import com.inbank.loanserver.exceptions.LoanOfferNotFoundException;
import com.inbank.loanserver.models.LoanContract;
import com.inbank.loanserver.models.LoanOffer;
import com.inbank.loanserver.models.LoanOfferStatus;
import com.inbank.loanserver.services.LoanContractService;
import com.inbank.loanserver.services.LoanOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.inbank.loanserver.utils.Constants.Data.DEFAULT_ITEMS_PER_PAGE;
import static com.inbank.loanserver.utils.LoanUtils.getSortOfColumn;

/**
 * Controller to handle loan contract related operations
 *
 * @author vinodjohn
 * @created 06.09.2024
 */
@RestController
@RequestMapping("/loan-contract")
public class LoanContractController {
    @Autowired
    private LoanContractService loanContractService;

    @Autowired
    private LoanOfferService loanOfferService;

    @GetMapping
    public ResponseEntity<ObjectListDto> getSortedLoanContractByPage(
            @RequestParam(name = "page", defaultValue = "0") int pageNum,
            @RequestParam(name = "items", defaultValue = DEFAULT_ITEMS_PER_PAGE) int totalItem,
            @RequestParam(name = "sort", defaultValue = "createdDate") String sort,
            @RequestParam(name = "order", defaultValue = "desc") String order) {
        Page<LoanContract> loanContractPage = loanContractService.findAllLoanContracts(PageRequest.of(pageNum
                , totalItem, getSortOfColumn(sort, order)));
        List<LoanContract> loanContractList = loanContractPage.stream()
                .collect(Collectors.toList());
        ObjectListDto objectListDto = new ObjectListDto(loanContractList, pageNum,
                loanContractPage.getTotalElements());

        return ResponseEntity.ok(objectListDto);
    }

    @GetMapping("/personal")
    public ResponseEntity<ObjectListDto> getSortedLoanContractOfPersonByPage(
            @RequestParam(name = "page", defaultValue = "0") int pageNum,
            @RequestParam(name = "items", defaultValue = DEFAULT_ITEMS_PER_PAGE) int totalItem,
            @RequestParam(name = "sort", defaultValue = "createdDate") String sort,
            @RequestParam(name = "order", defaultValue = "desc") String order) throws LoanContractNotFoundException {
        CustomUserDetails customUserDetails =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<LoanContract> loanContractPage = loanContractService.findLoanContractsByPerson(PageRequest.of(pageNum
                , totalItem, getSortOfColumn(sort, order)), customUserDetails.getPerson().getId());
        List<LoanContract> loanContractList = loanContractPage.stream()
                .collect(Collectors.toList());
        ObjectListDto objectListDto = new ObjectListDto(loanContractList, pageNum,
                loanContractPage.getTotalElements());

        return ResponseEntity.ok(objectListDto);
    }

    @PostMapping
    public ResponseEntity<?> createLoanContract(@RequestBody LoanContractRequest loanContractRequest)
            throws LoanOfferNotFoundException {
        LoanOffer loanOffer = loanOfferService.findLoanOfferById(loanContractRequest.loanOfferId());

        if (loanOffer != null) {
            loanOffer.setLoanOfferStatus(LoanOfferStatus.ACCEPTED);
            LoanContract loanContract = new LoanContract();
            loanContract.setLoanOffer(loanOffer);
            loanContract.setPeriod(loanContractRequest.period());
            loanContract.setInterestRate(loanContractRequest.interestRate());
            loanContract.setMonthlyAmount(BigDecimal.valueOf(loanContractRequest.monthlyAmount()));
            LoanContract newLoanContract = loanContractService.createLoanContract(loanContract);
            return new ResponseEntity<>(newLoanContract, HttpStatus.CREATED);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLoanContractById(@PathVariable UUID id) throws LoanContractNotFoundException {
        LoanContract loanContract = loanContractService.findLoanContractById(id);
        return ResponseEntity.ok(loanContract);
    }


    @PutMapping
    public ResponseEntity<?> updateLoanContract(@RequestBody LoanContract loanContract) throws LoanContractNotFoundException {
        LoanContract updatedLoanContract = loanContractService.updateLoanContract(loanContract);
        return ResponseEntity.ok(updatedLoanContract);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteLoanContract(@PathVariable UUID id) throws LoanContractNotFoundException {
        loanContractService.deleteLoanContractById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restoreLoanContract(@PathVariable UUID id) throws LoanContractNotFoundException {
        loanContractService.restoreLoanContractById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
