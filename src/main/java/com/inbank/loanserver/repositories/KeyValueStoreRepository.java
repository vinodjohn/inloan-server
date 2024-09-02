package com.inbank.loanserver.repositories;

import com.inbank.loanserver.models.KeyValueStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository to handle KeyValue Store related data queries
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
public interface KeyValueStoreRepository extends PagingAndSortingRepository<KeyValueStore, UUID>,
        JpaRepository<KeyValueStore, UUID> {
    Optional<KeyValueStore> findByKey(String key);
}
