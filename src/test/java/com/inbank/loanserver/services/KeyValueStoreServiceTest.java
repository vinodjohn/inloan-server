package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.KeyValueStoreNotFoundException;
import com.inbank.loanserver.models.KeyValueStore;
import com.inbank.loanserver.repositories.KeyValueStoreRepository;
import com.inbank.loanserver.services.implementations.KeyValueStoreServiceImpl;
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
 * Tests for KeyValueStoreService
 *
 * @author vinodjohn
 * @created 10.09.2024
 */
public class KeyValueStoreServiceTest {
    private static final String TEST_KEY = "Test_Key";
    private static final String MINIMUM_LOAN_AMOUNT = "MINIMUM_LOAN_AMOUNT";
    private static final String MAXIMUM_LOAN_AMOUNT = "MAXIMUM_LOAN_AMOUNT";
    private static final String CREDIT_COEFFICIENT = "CREDIT_COEFFICIENT";

    @InjectMocks
    private KeyValueStoreServiceImpl keyValueStoreService;

    @Mock
    private KeyValueStoreRepository keyValueStoreRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private KeyValueStore prepareKeyValueStore(String key) {
        KeyValueStore keyValueStore = new KeyValueStore();
        keyValueStore.setKey(key.toUpperCase());
        return keyValueStore;
    }

    @Test
    void testCreateKeyValueStore() {
        KeyValueStore keyValueStore = new KeyValueStore();
        keyValueStore.setKey(TEST_KEY);
        when(keyValueStoreRepository.saveAndFlush(any(KeyValueStore.class))).thenReturn(keyValueStore);
        KeyValueStore created = keyValueStoreService.createKeyValueStore(keyValueStore);
        assertNotNull(created);
        assertEquals(TEST_KEY.toUpperCase(), created.getKey());
        assertTrue(created.isActive());
    }

    @Test
    void testFindKeyValueStoreById() throws KeyValueStoreNotFoundException {
        UUID id = UUID.randomUUID();
        KeyValueStore keyValueStore = new KeyValueStore();
        keyValueStore.setId(id);
        when(keyValueStoreRepository.findById(id)).thenReturn(Optional.of(keyValueStore));
        KeyValueStore found = keyValueStoreService.findKeyValueStoreById(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    void testFindKeyValueStoreById_NotFound() {
        UUID id = UUID.randomUUID();
        when(keyValueStoreRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(KeyValueStoreNotFoundException.class, () -> keyValueStoreService.findKeyValueStoreById(id));
    }

    @Test
    void testFindKeyValueStoreByKey() throws KeyValueStoreNotFoundException {
        String key = TEST_KEY;
        KeyValueStore keyValueStore = prepareKeyValueStore(key);
        when(keyValueStoreRepository.findByKeyAndIsActiveTrue(key.toUpperCase())).thenReturn(Optional.of(keyValueStore));
        KeyValueStore found = keyValueStoreService.findKeyValueStoreByKey(key);
        assertNotNull(found);
        assertEquals(key.toUpperCase(), found.getKey());
    }

    @Test
    void testFindKeyValueStoreByKey_NotFound() {
        String key = TEST_KEY;
        when(keyValueStoreRepository.findByKeyAndIsActiveTrue(key.toUpperCase())).thenReturn(Optional.empty());
        assertThrows(KeyValueStoreNotFoundException.class, () -> keyValueStoreService.findKeyValueStoreByKey(key));
    }

    @Test
    void testFindAllKeyValueStores() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<KeyValueStore> page = new PageImpl<>(List.of(new KeyValueStore()));
        when(keyValueStoreRepository.findAll(pageable)).thenReturn(page);
        Page<KeyValueStore> result = keyValueStoreService.findAllKeyValueStores(pageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testUpdateKeyValueStore() throws KeyValueStoreNotFoundException {
        UUID id = UUID.randomUUID();
        KeyValueStore keyValueStore = new KeyValueStore();
        keyValueStore.setId(id);
        keyValueStore.setKey("Updated_Key");
        when(keyValueStoreRepository.findById(id)).thenReturn(Optional.of(keyValueStore));
        when(keyValueStoreRepository.saveAndFlush(any(KeyValueStore.class))).thenReturn(keyValueStore);
        KeyValueStore updated = keyValueStoreService.updateKeyValueStore(keyValueStore);
        assertNotNull(updated);
        assertEquals("UPDATED_KEY", updated.getKey());
    }

    @Test
    void testDeleteKeyValueStoreById() throws KeyValueStoreNotFoundException {
        UUID id = UUID.randomUUID();
        KeyValueStore keyValueStore = new KeyValueStore();
        keyValueStore.setId(id);
        when(keyValueStoreRepository.findById(id)).thenReturn(Optional.of(keyValueStore));
        keyValueStoreService.deleteKeyValueStoreById(id);
        assertFalse(keyValueStore.isActive());
    }

    @Test
    void testRestoreKeyValueStoreById() throws KeyValueStoreNotFoundException {
        UUID id = UUID.randomUUID();
        KeyValueStore keyValueStore = new KeyValueStore();
        keyValueStore.setId(id);
        keyValueStore.setActive(false);
        when(keyValueStoreRepository.findById(id)).thenReturn(Optional.of(keyValueStore));
        keyValueStoreService.restoreKeyValueStoreById(id);
        assertTrue(keyValueStore.isActive());
    }

    @Test
    void testGetMinimumLoanAmount() throws KeyValueStoreNotFoundException {
        KeyValueStore keyValueStore = prepareKeyValueStore(MINIMUM_LOAN_AMOUNT);
        when(keyValueStoreRepository.findByKeyAndIsActiveTrue(MINIMUM_LOAN_AMOUNT)).thenReturn(Optional.of(keyValueStore));
        KeyValueStore found = keyValueStoreService.getMinimumLoanAmount();
        assertNotNull(found);
        assertEquals(MINIMUM_LOAN_AMOUNT, found.getKey());
    }

    @Test
    void testGetMaximumLoanAmount() throws KeyValueStoreNotFoundException {
        KeyValueStore keyValueStore = prepareKeyValueStore(MAXIMUM_LOAN_AMOUNT);
        when(keyValueStoreRepository.findByKeyAndIsActiveTrue(MAXIMUM_LOAN_AMOUNT)).thenReturn(Optional.of(keyValueStore));
        KeyValueStore found = keyValueStoreService.getMaximumLoanAmount();
        assertNotNull(found);
        assertEquals(MAXIMUM_LOAN_AMOUNT, found.getKey());
    }

    @Test
    void testGetCreditCoefficient() throws KeyValueStoreNotFoundException {
        KeyValueStore keyValueStore = prepareKeyValueStore(CREDIT_COEFFICIENT);
        when(keyValueStoreRepository.findByKeyAndIsActiveTrue(CREDIT_COEFFICIENT)).thenReturn(Optional.of(keyValueStore));
        KeyValueStore found = keyValueStoreService.getCreditCoefficient();
        assertNotNull(found);
        assertEquals(CREDIT_COEFFICIENT, found.getKey());
    }
}