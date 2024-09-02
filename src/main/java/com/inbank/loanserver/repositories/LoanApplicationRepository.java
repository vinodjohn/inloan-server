package com.inbank.loanserver.repositories;

import com.inbank.loanserver.models.LoanApplication;
import com.inbank.loanserver.models.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

/**
 * Repository to handle Loan Book related data queries
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
public interface LoanApplicationRepository extends PagingAndSortingRepository<LoanApplication, UUID>,
        JpaRepository<LoanApplication, UUID> {
    Page<LoanApplication> findAllByPerson(Person person, Pageable pageable);
}
