package com.inbank.loanserver.repositories;

import com.inbank.loanserver.models.LoanBook;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

/**
 * Repository to handle LoanBook related data queries
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
public interface LoanBookRepository extends PagingAndSortingRepository<LoanBook, UUID> {
}
