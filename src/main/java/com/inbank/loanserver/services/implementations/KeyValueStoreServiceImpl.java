package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.exceptions.KeyValueStoreNotFoundException;
import com.inbank.loanserver.models.KeyValueStore;
import com.inbank.loanserver.repositories.KeyValueStoreRepository;
import com.inbank.loanserver.services.KeyValueStoreService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.inbank.loanserver.utils.Constants.KvStore.*;

/**
 * Implementation of KeyValueStoreService
 *
 * @author vinodjohn
 * @created 30.08.2024
 */
@Service
@Transactional
public class KeyValueStoreServiceImpl implements KeyValueStoreService {
    @Autowired
    private KeyValueStoreRepository keyValueStoreRepository;

    @Override
    public KeyValueStore createKeyValueStore(KeyValueStore keyValueStore) {
        String key = keyValueStore.getKey().trim().toUpperCase();
        keyValueStore.setKey(key);
        keyValueStore.setActive(true);
        return keyValueStoreRepository.save(keyValueStore);
    }

    @Override
    public KeyValueStore findKeyValueStoreById(UUID id) throws KeyValueStoreNotFoundException {
        Optional<KeyValueStore> optionalKeyValueStore = keyValueStoreRepository.findById(id);

        if (optionalKeyValueStore.isEmpty()) {
            throw new KeyValueStoreNotFoundException(id);
        }

        return optionalKeyValueStore.get();
    }

    @Override
    public KeyValueStore findKeyValueStoreByKey(String key) throws KeyValueStoreNotFoundException {
        Optional<KeyValueStore> optionalKeyValueStore = keyValueStoreRepository.findByKey(key);

        if (optionalKeyValueStore.isEmpty()) {
            throw new KeyValueStoreNotFoundException(key);
        }

        return optionalKeyValueStore.get();
    }

    @Override
    public Page<KeyValueStore> findAllKeyValueStores(Pageable pageable) {
        return keyValueStoreRepository.findAll(pageable);
    }

    @Override
    public KeyValueStore updateKeyValueStore(KeyValueStore keyValueStore) throws KeyValueStoreNotFoundException {
        if (findKeyValueStoreById(keyValueStore.getId()) != null) {
            keyValueStoreRepository.save(keyValueStore);
        }

        return null;
    }

    @Override
    public void deleteKeyValueStoreById(UUID id) throws KeyValueStoreNotFoundException {
        KeyValueStore keyValueStore = findKeyValueStoreById(id);
        keyValueStore.setActive(false);
        keyValueStoreRepository.save(keyValueStore);
    }

    @Override
    public void restoreKeyValueStoreById(UUID id) throws KeyValueStoreNotFoundException {
        KeyValueStore keyValueStore = findKeyValueStoreById(id);
        keyValueStore.setActive(true);
        keyValueStoreRepository.save(keyValueStore);
    }

    @Override
    public KeyValueStore getMinimumLoanAmount() throws KeyValueStoreNotFoundException {
        return findKeyValueStoreByKey(MINIMUM_LOAN_AMOUNT);
    }

    @Override
    public KeyValueStore getMaximumLoanAmount() throws KeyValueStoreNotFoundException {
        return findKeyValueStoreByKey(MAXIMUM_LOAN_AMOUNT);
    }

    @Override
    public KeyValueStore getMinimumLoanPeriod() throws KeyValueStoreNotFoundException {
        return findKeyValueStoreByKey(MINIMUM_LOAN_PERIOD);
    }

    @Override
    public KeyValueStore getMaximumLoanPeriod() throws KeyValueStoreNotFoundException {
        return findKeyValueStoreByKey(MAXIMUM_LOAN_PERIOD);
    }

    @Override
    public KeyValueStore getCreditCoefficient() throws KeyValueStoreNotFoundException {
        return findKeyValueStoreByKey(CREDIT_COEFFICIENT);
    }
}
