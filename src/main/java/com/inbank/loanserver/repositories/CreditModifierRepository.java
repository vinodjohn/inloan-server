package com.inbank.loanserver.repositories;

import com.inbank.loanserver.models.CreditModifier;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository to handle CreditModifier related data queries
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
@Repository
public interface CreditModifierRepository extends PagingAndSortingRepository<CreditModifier, UUID> {
}
