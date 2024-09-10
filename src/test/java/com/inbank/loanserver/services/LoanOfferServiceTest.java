package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.LoanOfferNotFoundException;
import com.inbank.loanserver.models.LoanApplication;
import com.inbank.loanserver.models.LoanOffer;
import com.inbank.loanserver.repositories.LoanOfferRepository;
import com.inbank.loanserver.services.implementations.LoanOfferServiceImpl;
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
import static org.mockito.Mockito.when;

/**
 * Tests for LoanOfferService
 *
 * @author vinodjohn
 * @created 10.09.2024
 */
public class LoanOfferServiceTest {

    @InjectMocks
    private LoanOfferServiceImpl loanOfferService;

    @Mock
    private LoanOfferRepository loanOfferRepository;

    private UUID id;
    private LoanOffer loanOffer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanOffer = new LoanOffer();
        id = UUID.randomUUID();
    }

    @Test
    void shouldCreateLoanOffer() {
        int loanAmount = 1000;
        loanOffer.setLoanAmount(loanAmount);
        when(loanOfferRepository.saveAndFlush(any(LoanOffer.class))).thenReturn(loanOffer);

        LoanOffer created = loanOfferService.createLoanOffer(loanOffer);

        assertNotNull(created);
        assertTrue(created.isActive());
        assertEquals(loanAmount, created.getLoanAmount());
    }

    @Test
    void shouldFindLoanOfferById() throws LoanOfferNotFoundException {
        loanOffer.setId(id);
        when(loanOfferRepository.findById(id)).thenReturn(Optional.of(loanOffer));

        LoanOffer found = loanOfferService.findLoanOfferById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    void shouldThrowExceptionWhenFindLoanOfferByIdNotFound() {
        when(loanOfferRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(LoanOfferNotFoundException.class, () -> loanOfferService.findLoanOfferById(id));
    }

    @Test
    void shouldFindLoanOffersByLoanApplication() {
        LoanApplication loanApplication = new LoanApplication();
        Pageable pageable = PageRequest.of(0, 10);
        Page<LoanOffer> page = new PageImpl<>(List.of(loanOffer));

        when(loanOfferRepository.findAllByLoanApplication(loanApplication, pageable)).thenReturn(page);

        Page<LoanOffer> result = loanOfferService.findLoanOffersByLoanApplication(pageable, loanApplication);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldFindAllLoanOffers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<LoanOffer> page = new PageImpl<>(List.of(new LoanOffer()));

        when(loanOfferRepository.findAll(pageable)).thenReturn(page);

        Page<LoanOffer> result = loanOfferService.findAllLoanOffers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldUpdateLoanOffer() throws LoanOfferNotFoundException {
        loanOffer.setId(id);
        loanOffer.setLoanAmount(2000);

        when(loanOfferRepository.findById(id)).thenReturn(Optional.of(loanOffer));
        when(loanOfferRepository.saveAndFlush(any(LoanOffer.class))).thenReturn(loanOffer);

        LoanOffer updated = loanOfferService.updateLoanOffer(loanOffer);

        assertNotNull(updated);
        assertEquals(2000, updated.getLoanAmount());
    }

    @Test
    void shouldDeactivateLoanOffer() throws LoanOfferNotFoundException {
        loanOffer.setId(id);

        when(loanOfferRepository.findById(id)).thenReturn(Optional.of(loanOffer));
        when(loanOfferRepository.saveAndFlush(any(LoanOffer.class))).thenReturn(loanOffer);

        loanOfferService.deleteLoanOfferById(id);

        assertFalse(loanOffer.isActive());
    }

    @Test
    void shouldRestoreLoanOffer() throws LoanOfferNotFoundException {
        loanOffer.setId(id);
        loanOffer.setActive(false);

        when(loanOfferRepository.findById(id)).thenReturn(Optional.of(loanOffer));
        when(loanOfferRepository.saveAndFlush(any(LoanOffer.class))).thenReturn(loanOffer);

        loanOfferService.restoreLoanOfferById(id);

        assertTrue(loanOffer.isActive());
    }
}