package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.models.CreditModifier;
import com.inbank.loanserver.repositories.CreditModifierRepository;
import com.inbank.loanserver.services.implementations.CreditModifierServiceImpl;
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
 * Tests for CreditModifierService
 *
 * @author vinodjohn
 * @created 10.09.2024
 */
public class CreditModifierServiceTest {
    private static final String TEST_MODIFIER_NAME = "TestModifier";

    @InjectMocks
    private CreditModifierServiceImpl creditModifierService;

    @Mock
    private CreditModifierRepository creditModifierRepository;

    private CreditModifier creditModifier;
    private UUID testId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        creditModifier = createTestModifier();
        testId = UUID.randomUUID();
    }

    private CreditModifier createTestModifier() {
        CreditModifier creditModifier = new CreditModifier();
        creditModifier.setName(TEST_MODIFIER_NAME);
        return creditModifier;
    }

    @Test
    void testCreateCreditModifier() {
        when(creditModifierRepository.saveAndFlush(any(CreditModifier.class))).thenReturn(creditModifier);

        CreditModifier created = creditModifierService.createCreditModifier(creditModifier);

        assertNotNull(created);
        assertEquals(TEST_MODIFIER_NAME.toUpperCase(), created.getName());
        assertTrue(created.isActive());
    }

    @Test
    void testFindCreditModifierById() throws CreditModifierNotFoundException {
        creditModifier.setId(testId);
        when(creditModifierRepository.findById(testId)).thenReturn(Optional.of(creditModifier));

        CreditModifier found = creditModifierService.findCreditModifierById(testId);

        assertNotNull(found);
        assertEquals(testId, found.getId());
    }

    @Test
    void testFindCreditModifierById_NotFound() {
        when(creditModifierRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(CreditModifierNotFoundException.class, () -> creditModifierService.findCreditModifierById(testId));
    }

    @Test
    void testFindCreditModifierByName() throws CreditModifierNotFoundException {
        when(creditModifierRepository.findByNameAndIsActiveTrue(TEST_MODIFIER_NAME.toUpperCase()))
                .thenReturn(Optional.of(creditModifier));

        CreditModifier found = creditModifierService.findCreditModifierByName(TEST_MODIFIER_NAME);

        assertNotNull(found);
        assertEquals(TEST_MODIFIER_NAME, found.getName());
    }

    @Test
    void testFindCreditModifierByName_NotFound() {
        when(creditModifierRepository.findByNameAndIsActiveTrue(TEST_MODIFIER_NAME.toUpperCase()))
                .thenReturn(Optional.empty());

        assertThrows(CreditModifierNotFoundException.class, () ->
                creditModifierService.findCreditModifierByName(TEST_MODIFIER_NAME));
    }

    @Test
    void testFindRandomCreditModifier() throws CreditModifierNotFoundException {
        when(creditModifierRepository.findRandom()).thenReturn(Optional.of(creditModifier));

        CreditModifier found = creditModifierService.findRandomCreditModifier();

        assertNotNull(found);
    }

    @Test
    void testFindRandomCreditModifier_NotFound() {
        when(creditModifierRepository.findRandom()).thenReturn(Optional.empty());

        assertThrows(CreditModifierNotFoundException.class, () -> creditModifierService.findRandomCreditModifier());
    }

    @Test
    void testFindAllCreditModifiers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CreditModifier> page = new PageImpl<>(List.of(creditModifier));
        when(creditModifierRepository.findAll(pageable)).thenReturn(page);

        Page<CreditModifier> result = creditModifierService.findAllCreditModifiers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testUpdateCreditModifier() throws CreditModifierNotFoundException {
        creditModifier.setId(testId);
        creditModifier.setName("UpdatedModifier");
        when(creditModifierRepository.findById(testId)).thenReturn(Optional.of(creditModifier));
        when(creditModifierRepository.saveAndFlush(any(CreditModifier.class))).thenReturn(creditModifier);

        CreditModifier updated = creditModifierService.updateCreditModifier(creditModifier);

        assertNotNull(updated);
        assertEquals("UPDATEDMODIFIER", updated.getName());
    }

    @Test
    void testDeleteCreditModifierById() throws CreditModifierNotFoundException {
        creditModifier.setId(testId);
        when(creditModifierRepository.findById(testId)).thenReturn(Optional.of(creditModifier));
        when(creditModifierRepository.saveAndFlush(any(CreditModifier.class))).thenReturn(creditModifier);

        creditModifierService.deleteCreditModifierById(testId);

        assertFalse(creditModifier.isActive());
    }

    @Test
    void testRestoreCreditModifierById() throws CreditModifierNotFoundException {
        creditModifier.setId(testId);
        creditModifier.setActive(false);
        when(creditModifierRepository.findById(testId)).thenReturn(Optional.of(creditModifier));
        when(creditModifierRepository.saveAndFlush(any(CreditModifier.class))).thenReturn(creditModifier);

        creditModifierService.restoreCreditModifierById(testId);

        assertTrue(creditModifier.isActive());
    }
}