package com.inbank.loanserver.repositories;

import com.inbank.loanserver.models.LoanApplication;
import com.inbank.loanserver.models.LoanOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository to handle Loan Offer related data queries
 *
 * @author vinodjohn
 * @created 30.08.2024
 */
@Repository
public interface LoanOfferRepository extends PagingAndSortingRepository<LoanOffer, UUID>,
        JpaRepository<LoanOffer, UUID> {
    Page<LoanOffer> findAllByLoanApplication(LoanApplication loanApplication, Pageable pageable);
}
