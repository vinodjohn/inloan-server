package com.inbank.loanserver.repositories;

import com.inbank.loanserver.models.CreditModifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository to handle Credit Modifier related data queries
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
@Repository
public interface CreditModifierRepository extends PagingAndSortingRepository<CreditModifier, UUID>,
        JpaRepository<CreditModifier, UUID> {
    Optional<CreditModifier> findByNameAndIsActiveTrue(String name);

    @Query(value = "SELECT * FROM credit_modifier WHERE is_active = true ORDER BY RANDOM() LIMIT 1", nativeQuery =
            true)
    Optional<CreditModifier> findRandom();
}
