package com.inbank.loanserver.controllers;

import com.inbank.loanserver.dtos.ObjectListDto;
import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.models.CreditModifier;
import com.inbank.loanserver.services.CreditModifierService;
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
 * Controller to handle credit modifier related operations
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
@RestController
@RequestMapping("/credit-modifier")
public class CreditModifierController {
    @Autowired
    private CreditModifierService creditModifierService;

    @GetMapping
    public ResponseEntity<ObjectListDto> getSortedCreditModifierByPage(
            @RequestParam(name = "page", defaultValue = "0") int pageNum,
            @RequestParam(name = "items", defaultValue = DEFAULT_ITEMS_PER_PAGE) int totalItem,
            @RequestParam(name = "sort", defaultValue = "createdDate") String sort,
            @RequestParam(name = "order", defaultValue = "desc") String order) {
        Page<CreditModifier> creditModifierPage = creditModifierService.findAllCreditModifiers(PageRequest.of(pageNum
                , totalItem, getSortOfColumn(sort, order)));
        List<CreditModifier> creditModifierList = creditModifierPage.stream()
                .collect(Collectors.toList());
        ObjectListDto objectListDto = new ObjectListDto(creditModifierList, pageNum,
                creditModifierPage.getTotalElements());
        return ResponseEntity.ok(objectListDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCreditModifierById(@PathVariable UUID id) throws CreditModifierNotFoundException {
        CreditModifier creditModifier = creditModifierService.findCreditModifierById(id);
        return ResponseEntity.ok(creditModifier);
    }

    @PostMapping
    public ResponseEntity<?> createCreditModifier(@Valid @RequestBody CreditModifier creditModifier) {
        CreditModifier newCreditModifier = creditModifierService.createCreditModifier(creditModifier);
        return new ResponseEntity<>(newCreditModifier, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateCreditModifier(@Valid @RequestBody CreditModifier creditModifier) throws CreditModifierNotFoundException {
        CreditModifier updatedCreditModifier = creditModifierService.updateCreditModifier(creditModifier);
        return ResponseEntity.ok(updatedCreditModifier);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteCreditModifier(@PathVariable UUID id) throws CreditModifierNotFoundException {
        creditModifierService.deleteCreditModifierById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restoreCreditModifier(@PathVariable UUID id) throws CreditModifierNotFoundException {
        creditModifierService.restoreCreditModifierById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
