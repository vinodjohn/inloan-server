package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.LoanApplicationNotFoundException;
import com.inbank.loanserver.models.LoanApplication;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.repositories.LoanApplicationRepository;
import com.inbank.loanserver.services.implementations.LoanApplicationServiceImpl;
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
 * Tests for LoanApplicationService
 *
 * @author vinodjohn
 * @created 10.09.2024
 */
public class LoanApplicationServiceTest {
    private static final int REQUEST_AMOUNT_1000 = 1000;
    private static final int REQUEST_AMOUNT_2000 = 2000;

    @InjectMocks
    private LoanApplicationServiceImpl loanApplicationService;

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private LoanApplication createMockLoanApplication(UUID id, int requestAmount, boolean isActive) {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setId(id);
        loanApplication.setRequestAmount(requestAmount);
        loanApplication.setActive(isActive);
        return loanApplication;
    }

    @Test
    void shouldCreateLoanApplication() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setRequestAmount(REQUEST_AMOUNT_1000);
        when(loanApplicationRepository.saveAndFlush(any(LoanApplication.class))).thenReturn(loanApplication);

        LoanApplication created = loanApplicationService.createLoanApplication(loanApplication);

        assertNotNull(created);
        assertTrue(created.isActive());
        assertEquals(REQUEST_AMOUNT_1000, created.getRequestAmount());
    }

    @Test
    void shouldFindLoanApplicationById() throws LoanApplicationNotFoundException {
        UUID id = UUID.randomUUID();
        LoanApplication loanApplication = createMockLoanApplication(id, 0, true);
        when(loanApplicationRepository.findById(id)).thenReturn(Optional.of(loanApplication));

        LoanApplication found = loanApplicationService.findLoanApplicationById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    void shouldThrowExceptionWhenLoanApplicationNotFoundById() {
        UUID id = UUID.randomUUID();
        when(loanApplicationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(LoanApplicationNotFoundException.class, () -> loanApplicationService.findLoanApplicationById(id));
    }

    @Test
    void shouldFindLoanApplicationsByPerson() {
        Person person = new Person();
        Pageable pageable = PageRequest.of(0, 10);
        Page<LoanApplication> page = new PageImpl<>(List.of(new LoanApplication()));
        when(loanApplicationRepository.findAllByPerson(person, pageable)).thenReturn(page);

        Page<LoanApplication> result = loanApplicationService.findLoanApplicationsByPerson(pageable, person);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldFindAllLoanApplications() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<LoanApplication> page = new PageImpl<>(List.of(new LoanApplication()));
        when(loanApplicationRepository.findAll(pageable)).thenReturn(page);

        Page<LoanApplication> result = loanApplicationService.findAllLoanApplications(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldUpdateLoanApplication() throws LoanApplicationNotFoundException {
        UUID id = UUID.randomUUID();
        LoanApplication loanApplication = createMockLoanApplication(id, REQUEST_AMOUNT_2000, true);
        when(loanApplicationRepository.findById(id)).thenReturn(Optional.of(loanApplication));
        when(loanApplicationRepository.saveAndFlush(any(LoanApplication.class))).thenReturn(loanApplication);

        LoanApplication updated = loanApplicationService.updateLoanApplication(loanApplication);

        assertNotNull(updated);
        assertEquals(REQUEST_AMOUNT_2000, updated.getRequestAmount());
    }

    @Test
    void shouldDeleteLoanApplicationById() throws LoanApplicationNotFoundException {
        UUID id = UUID.randomUUID();
        LoanApplication loanApplication = createMockLoanApplication(id, 0, true);
        when(loanApplicationRepository.findById(id)).thenReturn(Optional.of(loanApplication));
        when(loanApplicationRepository.saveAndFlush(any(LoanApplication.class))).thenReturn(loanApplication);

        loanApplicationService.deleteLoanApplicationById(id);
        assertFalse(loanApplication.isActive());
    }

    @Test
    void shouldRestoreLoanApplicationById() throws LoanApplicationNotFoundException {
        UUID id = UUID.randomUUID();
        LoanApplication loanApplication = createMockLoanApplication(id, 0, false);
        when(loanApplicationRepository.findById(id)).thenReturn(Optional.of(loanApplication));
        when(loanApplicationRepository.saveAndFlush(any(LoanApplication.class))).thenReturn(loanApplication);

        loanApplicationService.restoreLoanApplicationById(id);
        assertTrue(loanApplication.isActive());
    }
}