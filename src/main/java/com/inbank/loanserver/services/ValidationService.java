package com.inbank.loanserver.services;

import com.inbank.loanserver.dtos.ChangePassword;
import com.inbank.loanserver.dtos.LoanRequest;
import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.exceptions.KeyValueStoreNotFoundException;
import com.inbank.loanserver.exceptions.LoanValidationException;
import com.inbank.loanserver.exceptions.PersonNotFoundException;
import com.inbank.loanserver.models.CreditModifier;
import com.inbank.loanserver.models.KeyValueStore;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.models.Role;

/**
 * Service to validate different data
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
public interface ValidationService {
    /**
     * To validate credit modifier
     *
     * @param creditModifier CreditModifier object
     */
    void validateCreditModifier(CreditModifier creditModifier) throws LoanValidationException, CreditModifierNotFoundException;

    /**
     * To validate key value store
     *
     * @param keyValueStore KeyValueStore object
     */
    void validateKeyValueStore(KeyValueStore keyValueStore) throws LoanValidationException, KeyValueStoreNotFoundException;

    /**
     * To validate loan request
     *
     * @param loanRequest LoanRequest object
     */
    void validateLoanRequest(LoanRequest loanRequest) throws KeyValueStoreNotFoundException, LoanValidationException;

    /**
     * To validate person
     *
     * @param person Person object
     */
    void validatePerson(Person person) throws LoanValidationException, PersonNotFoundException, CreditModifierNotFoundException;

    /**
     * To validate role
     *
     * @param role Role object
     */
    void validateRole(Role role) throws LoanValidationException;

    /**
     * To validate Change Password
     *
     * @param changePassword Change Password
     */
    void validateChangePassword(ChangePassword changePassword);
}
