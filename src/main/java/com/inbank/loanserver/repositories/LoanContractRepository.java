package com.inbank.loanserver.repositories;

import com.inbank.loanserver.models.LoanContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository to handle loan contract related data queries
 *
 * @author vinodjohn
 * @created 05.09.2024
 */
@Repository
public interface LoanContractRepository extends PagingAndSortingRepository<LoanContract, UUID>,
        JpaRepository<LoanContract, UUID> {
    @Query("SELECT lc FROM LoanContract lc WHERE lc.loanOffer.loanApplication.person.id = :personId")
    Page<LoanContract> findAllByPerson(@Param("personId") UUID personId, Pageable pageable);
}
