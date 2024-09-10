package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.LoanContractNotFoundException;
import com.inbank.loanserver.exceptions.LoanOfferNotFoundException;
import com.inbank.loanserver.models.LoanContract;
import com.inbank.loanserver.models.LoanOffer;
import com.inbank.loanserver.repositories.LoanContractRepository;
import com.inbank.loanserver.services.implementations.LoanContractServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Tests for LoanContractService
 *
 * @author vinodjohn
 * @created 10.09.2024
 */
public class LoanContractServiceTest {
    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 10;

    @InjectMocks
    private LoanContractServiceImpl loanContractService;

    @Mock
    private LoanContractRepository loanContractRepository;

    @Mock
    private LoanOfferService loanOfferService;

    private UUID id;
    private LoanContract loanContract;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        loanContract = createLoanContract(id);
        pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
    }

    private LoanContract createLoanContract(UUID id) {
        LoanContract loanContract = new LoanContract();
        loanContract.setId(id);
        return loanContract;
    }

    @Test
    void testCreateLoanContract() throws LoanOfferNotFoundException {
        LoanContract loanContract = new LoanContract();
        loanContract.setLoanOffer(new LoanOffer());
        doNothing().when(loanOfferService).changeLoanOfferSiblingStatus(any(), any());
        when(loanContractRepository.saveAndFlush(any(LoanContract.class))).thenReturn(loanContract);
        LoanContract created = loanContractService.createLoanContract(loanContract);
        assertNotNull(created);
        assertTrue(created.isActive());
    }

    @Test
    void testFindLoanContractById() throws LoanContractNotFoundException {
        when(loanContractRepository.findById(id)).thenReturn(Optional.of(loanContract));
        LoanContract foundLoanContract = loanContractService.findLoanContractById(id);
        assertNotNull(foundLoanContract);
        assertEquals(id, foundLoanContract.getId());
    }

    @Test
    void testFindLoanContractById_NotFound() {
        when(loanContractRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(LoanContractNotFoundException.class, () -> loanContractService.findLoanContractById(id));
    }

    @Test
    void testFindLoanContractsByPerson() {
        Page<LoanContract> page = new PageImpl<>(List.of(new LoanContract()));
        UUID personId = UUID.randomUUID();
        when(loanContractRepository.findAllByPerson(personId, pageable)).thenReturn(page);
        Page<LoanContract> result = loanContractService.findLoanContractsByPerson(pageable, personId);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindAllLoanContracts() {
        Page<LoanContract> page = new PageImpl<>(List.of(new LoanContract()));
        when(loanContractRepository.findAll(pageable)).thenReturn(page);
        Page<LoanContract> result = loanContractService.findAllLoanContracts(pageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testUpdateLoanContract() throws LoanContractNotFoundException {
        loanContract.setInterestRate(5.0f);
        when(loanContractRepository.findById(id)).thenReturn(Optional.of(loanContract));
        when(loanContractRepository.saveAndFlush(any(LoanContract.class))).thenReturn(loanContract);
        LoanContract updatedLoanContract = loanContractService.updateLoanContract(loanContract);
        assertNotNull(updatedLoanContract);
        assertEquals(5.0f, updatedLoanContract.getInterestRate());
    }

    @Test
    void testDeleteLoanContractById() throws LoanContractNotFoundException {
        when(loanContractRepository.findById(id)).thenReturn(Optional.of(loanContract));
        loanContractService.deleteLoanContractById(id);
        assertFalse(loanContract.isActive());
    }

    @Test
    void testRestoreLoanContractById() throws LoanContractNotFoundException {
        loanContract.setActive(false);
        when(loanContractRepository.findById(id)).thenReturn(Optional.of(loanContract));
        loanContractService.restoreLoanContractById(id);
        assertTrue(loanContract.isActive());
    }
}