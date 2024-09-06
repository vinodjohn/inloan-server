package com.inbank.loanserver.controllers;

import com.inbank.loanserver.dtos.ObjectListDto;
import com.inbank.loanserver.exceptions.LoanOfferNotFoundException;
import com.inbank.loanserver.models.LoanOffer;
import com.inbank.loanserver.services.LoanOfferService;
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
 * Controller to handle loan offer related operations
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
@RestController
@RequestMapping("/loan-offer")
public class LoanOfferController {
    @Autowired
    private LoanOfferService loanOfferService;

    @GetMapping
    public ResponseEntity<ObjectListDto> getSortedLoanOfferByPage(
            @RequestParam(name = "page", defaultValue = "0") int pageNum,
            @RequestParam(name = "items", defaultValue = DEFAULT_ITEMS_PER_PAGE) int totalItem,
            @RequestParam(name = "sort", defaultValue = "createdDate") String sort,
            @RequestParam(name = "order", defaultValue = "desc") String order) {
        Page<LoanOffer> loanOfferPage = loanOfferService.findAllLoanOffers(PageRequest.of(pageNum
                , totalItem, getSortOfColumn(sort, order)));
        List<LoanOffer> loanOfferList = loanOfferPage.stream()
                .collect(Collectors.toList());
        ObjectListDto objectListDto = new ObjectListDto(loanOfferList, pageNum,
                loanOfferPage.getTotalElements());

        return ResponseEntity.ok(objectListDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLoanOfferById(@PathVariable UUID id) throws LoanOfferNotFoundException {
        LoanOffer loanOffer = loanOfferService.findLoanOfferById(id);
        return ResponseEntity.ok(loanOffer);
    }

    @PutMapping
    public ResponseEntity<?> updateLoanOffer(@RequestBody LoanOffer loanOffer) throws LoanOfferNotFoundException {
        LoanOffer updatedLoanOffer = loanOfferService.updateLoanOffer(loanOffer);
        return ResponseEntity.ok(updatedLoanOffer);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteLoanOffer(@PathVariable UUID id) throws LoanOfferNotFoundException {
        loanOfferService.deleteLoanOfferById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restoreLoanOffer(@PathVariable UUID id) throws LoanOfferNotFoundException {
        loanOfferService.restoreLoanOfferById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
