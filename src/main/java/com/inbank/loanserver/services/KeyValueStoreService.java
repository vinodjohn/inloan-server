package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.KeyValueStoreNotFoundException;
import com.inbank.loanserver.models.KeyValueStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service to handle Key Value Store related operations
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
public interface KeyValueStoreService {
    /**
     * To create a new key value
     *
     * @param keyValueStore KeyValueStore
     * @return KeyValueStore
     */
    KeyValueStore createKeyValueStore(KeyValueStore keyValueStore);

    /**
     * To find a key value by ID
     *
     * @param id ID of a KeyValueStore
     * @return KeyValueStore
     */
    KeyValueStore findKeyValueStoreById(UUID id) throws KeyValueStoreNotFoundException;

    /**
     * To find a key value by Key
     *
     * @param key Key of a KeyValueStore
     * @return KeyValueStore
     */
    KeyValueStore findKeyValueStoreByKey(String key) throws KeyValueStoreNotFoundException;

    /**
     * To find all key values
     *
     * @param pageable Pageable of Key value store
     * @return page of KeyValueStore
     */
    Page<KeyValueStore> findAllKeyValueStores(Pageable pageable);

    /**
     * To update an existing key value
     *
     * @param keyValueStore KeyValueStore keyValueStore
     * @return KeyValueStore
     */
    KeyValueStore updateKeyValueStore(KeyValueStore keyValueStore) throws KeyValueStoreNotFoundException;

    /**
     * To delete a key value by ID
     *
     * @param id KeyValueStore ID
     */
    void deleteKeyValueStoreById(UUID id) throws KeyValueStoreNotFoundException;

    /**
     * To restore a key value by ID
     *
     * @param id KeyValueStore ID
     */
    void restoreKeyValueStoreById(UUID id) throws KeyValueStoreNotFoundException;

    /**
     * To get minimum loan amount
     *
     * @return KeyValueStore
     */
    KeyValueStore getMinimumLoanAmount() throws KeyValueStoreNotFoundException;

    /**
     * To get maximum loan amount
     *
     * @return KeyValueStore
     */
    KeyValueStore getMaximumLoanAmount() throws KeyValueStoreNotFoundException;

    /**
     * To get a minimum loan period
     *
     * @return KeyValueStore
     */
    KeyValueStore getMinimumLoanPeriod() throws KeyValueStoreNotFoundException;

    /**
     * To get a maximum loan period
     *
     * @return KeyValueStore
     */
    KeyValueStore getMaximumLoanPeriod() throws KeyValueStoreNotFoundException;

    /**
     * To get credit coefficient
     *
     * @return KeyValueStore
     */
    KeyValueStore getCreditCoefficient() throws KeyValueStoreNotFoundException;
}
