package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.models.CreditModifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service to handle Credit Modifier related operations
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
public interface CreditModifierService {
    /**
     * To create a new credit modifier
     *
     * @param creditModifier CreditModifier
     * @return CreditModifier
     */
    CreditModifier createCreditModifier(CreditModifier creditModifier);

    /**
     * To find a Credit Modifier by ID
     *
     * @param id ID of a CreditModifier
     * @return CreditModifier
     */
    CreditModifier findCreditModifierById(UUID id) throws CreditModifierNotFoundException;

    /**
     * To find a Credit Modifier by Name
     *
     * @param name Name of a CreditModifier
     * @return CreditModifier
     */
    CreditModifier findCreditModifierByName(String name) throws CreditModifierNotFoundException;

    /**
     * To find all credit modifiers
     *
     * @param pageable Pageable of credit modifier
     * @return page of creditModifier
     */
    Page<CreditModifier> findAllCreditModifiers(Pageable pageable);

    /**
     * To update an existing credit modifier
     *
     * @param creditModifier CreditModifier creditModifier
     * @return CreditModifier
     */
    CreditModifier updateCreditModifier(CreditModifier creditModifier) throws CreditModifierNotFoundException;

    /**
     * To delete a credit modifier by ID
     *
     * @param id CreditModifier ID
     */
    void deleteCreditModifierById(UUID id) throws CreditModifierNotFoundException;

    /**
     * To restore a credit modifier by ID
     *
     * @param id CreditModifier ID
     */
    void restoreCreditModifierById(UUID id) throws CreditModifierNotFoundException;
}
