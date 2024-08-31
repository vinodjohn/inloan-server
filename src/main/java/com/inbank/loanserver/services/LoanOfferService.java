package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.LoanOfferNotFoundException;
import com.inbank.loanserver.models.LoanApplication;
import com.inbank.loanserver.models.LoanOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service to handle Loan Offer related operations
 *
 * @author vinodjohn
 * @created 30.08.2024
 */
public interface LoanOfferService {
    /**
     * To create a new loan offer
     *
     * @param loanOffer LoanOffer
     * @return LoanOffer
     */
    LoanOffer createLoanOffer(LoanOffer loanOffer);

    /**
     * To find a Loan Offer by ID
     *
     * @param id ID of a LoanOffer
     * @return LoanOffer
     */
    LoanOffer findLoanOfferById(UUID id) throws LoanOfferNotFoundException;

    /**
     * To find all loan offers of a loan application
     *
     * @param pageable        Pageable of Loan Offers
     * @param loanApplication LoanApplication of loan offer
     * @return Page of loan offer
     */
    Page<LoanOffer> findLoanOffersByLoanApplication(Pageable pageable, LoanApplication loanApplication) throws LoanOfferNotFoundException;

    /**
     * To find all loan offers
     *
     * @param pageable Pageable of Loan Offers
     * @return page of loan offer
     */
    Page<LoanOffer> findAllLoanOffers(Pageable pageable);

    /**
     * To update an existing loan offer
     *
     * @param loanOffer LoanOffer
     * @return LoanOffer
     */
    LoanOffer updateLoanOffer(LoanOffer loanOffer) throws LoanOfferNotFoundException;

    /**
     * To delete a loan offer by ID
     *
     * @param id LoanOffer ID
     */
    void deleteLoanOfferById(UUID id) throws LoanOfferNotFoundException;

    /**
     * To restore a loan offer by ID
     *
     * @param id LoanOffer ID
     */
    void restoreLoanOfferById(UUID id) throws LoanOfferNotFoundException;
}
