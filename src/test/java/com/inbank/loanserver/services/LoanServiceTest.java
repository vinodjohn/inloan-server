package com.inbank.loanserver.services;

import com.inbank.loanserver.dtos.LoanResponse;
import com.inbank.loanserver.exceptions.KeyValueStoreNotFoundException;
import com.inbank.loanserver.models.*;
import com.inbank.loanserver.services.implementations.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests for LoanService
 *
 * @author vinodjohn
 * @created 10.09.2024
 */
public class LoanServiceTest {
    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private LoanOfferService loanOfferService;

    @Mock
    private KeyValueStoreService keyValueStoreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetLoanDecision() throws KeyValueStoreNotFoundException {
        LoanApplication loanApplication = createLoanApplication();
        mockKeyValueStoreService();

        LoanOffer loanOffer = createLoanOffer();
        when(loanOfferService.createLoanOffer(any(LoanOffer.class))).thenReturn(loanOffer);

        LoanResponse loanResponse = loanService.getLoanDecision(loanApplication);

        assertNotNull(loanResponse);
        assertEquals("POSITIVE", loanResponse.loanDecisionStatus());
        assertEquals(0, loanResponse.loanOfferDtos().size());
    }

    @Test
    void testGetLoanDecision_KeyValueStoreNotFoundException() throws KeyValueStoreNotFoundException {
        LoanApplication loanApplication = createLoanApplication();
        when(keyValueStoreService.getCreditCoefficient()).thenThrow(new KeyValueStoreNotFoundException("test-key"));

        assertThrows(KeyValueStoreNotFoundException.class, () -> loanService.getLoanDecision(loanApplication));
    }

    // PRIVATE METHODS //
    private LoanApplication createLoanApplication() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setId(UUID.randomUUID());
        loanApplication.setRequestAmount(10000);
        loanApplication.setRequestPeriod(12);
        Person person = new Person();
        CreditModifier creditModifier = new CreditModifier();
        creditModifier.setValue(10);
        person.setCreditModifier(creditModifier);
        loanApplication.setPerson(person);
        return loanApplication;
    }

    private void mockKeyValueStoreService() throws KeyValueStoreNotFoundException {
        when(keyValueStoreService.getCreditCoefficient()).thenReturn(createKeyValueStore(1));
        when(keyValueStoreService.getMaximumLoanPeriod()).thenReturn(createKeyValueStore(24));
        when(keyValueStoreService.getMaximumLoanAmount()).thenReturn(createKeyValueStore(20000));
        when(keyValueStoreService.getMinimumLoanAmount()).thenReturn(createKeyValueStore(5000));
        when(keyValueStoreService.getMinimumLoanPeriod()).thenReturn(createKeyValueStore(6));
    }

    private KeyValueStore createKeyValueStore(int value) {
        KeyValueStore keyValueStore = new KeyValueStore();
        keyValueStore.setValue(value);
        return keyValueStore;
    }

    private LoanOffer createLoanOffer() {
        LoanOffer loanOffer = new LoanOffer();
        loanOffer.setId(UUID.randomUUID());
        loanOffer.setLoanAmount(10000);
        loanOffer.setMinPeriod(12);
        loanOffer.setMaxPeriod(24);
        loanOffer.setLoanOfferType(LoanOfferType.BASIC);
        return loanOffer;
    }
}