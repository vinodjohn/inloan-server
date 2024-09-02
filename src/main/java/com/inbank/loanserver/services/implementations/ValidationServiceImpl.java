package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.dtos.LoanRequest;
import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.exceptions.KeyValueStoreNotFoundException;
import com.inbank.loanserver.exceptions.LoanValidationException;
import com.inbank.loanserver.exceptions.PersonNotFoundException;
import com.inbank.loanserver.models.CreditModifier;
import com.inbank.loanserver.models.KeyValueStore;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.services.CreditModifierService;
import com.inbank.loanserver.services.KeyValueStoreService;
import com.inbank.loanserver.services.PersonService;
import com.inbank.loanserver.services.ValidationService;
import com.inbank.loanserver.utils.LoanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * Implementation of ValidationService
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
@Service
@Slf4j
public class ValidationServiceImpl implements ValidationService {
    @Autowired
    private CreditModifierService creditModifierService;

    @Autowired
    private KeyValueStoreService keyValueStoreService;

    @Autowired
    private PersonService personService;

    @Override
    public void validateCreditModifier(CreditModifier creditModifier) throws LoanValidationException {
        if (creditModifier.getName().isEmpty()) {
            throw new LoanValidationException(getExceptionMessage(CreditModifier.class, "name",
                    false));
        }

        if (creditModifier.getValue() < 0) {
            throw new LoanValidationException(getExceptionMessage(CreditModifier.class, "value",
                    false));
        }

        try {
            creditModifierService.findCreditModifierByName(creditModifier.getName().toUpperCase());
            throw new LoanValidationException(getExceptionMessage(CreditModifier.class, "name",
                    true));
        } catch (CreditModifierNotFoundException e) {
            log.info("Credit Modifier with name {} not found", creditModifier.getName());
        }

    }

    @Override
    public void validateKeyValueStore(KeyValueStore keyValueStore) throws LoanValidationException {
        if (keyValueStore.getKey().isEmpty()) {
            throw new LoanValidationException(getExceptionMessage(KeyValueStore.class, "key",
                    false));
        }

        if (keyValueStore.getValue() < 0) {
            throw new LoanValidationException(getExceptionMessage(KeyValueStore.class, "value",
                    false));
        }

        try {
            keyValueStoreService.findKeyValueStoreByKey(keyValueStore.getKey());
            throw new LoanValidationException(getExceptionMessage(KeyValueStore.class, "key",
                    true));
        } catch (KeyValueStoreNotFoundException e) {
            log.info("Key store with key {} not found", keyValueStore.getKey());
        }
    }

    @Override
    public void validateLoanRequest(LoanRequest loanRequest) throws KeyValueStoreNotFoundException,
            LoanValidationException {
        if (loanRequest.loanAmount() < keyValueStoreService.getMinimumLoanAmount().getValue()
                || loanRequest.loanAmount() > keyValueStoreService.getMaximumLoanAmount().getValue()) {
            throw new LoanValidationException(getExceptionMessage(LoanRequest.class, "loan amount",
                    false));
        }

        if (loanRequest.loanPeriod() < keyValueStoreService.getMinimumLoanPeriod().getValue()
                || loanRequest.loanPeriod() > keyValueStoreService.getMaximumLoanPeriod().getValue()) {
            throw new LoanValidationException(getExceptionMessage(LoanRequest.class, "loan amount",
                    false));
        }
    }

    @Override
    public void validatePerson(Person person) throws LoanValidationException {
        if (person.getFirstName().isEmpty()) {
            throw new LoanValidationException(getExceptionMessage(Person.class, "first name",
                    false));
        }

        if (LoanUtils.isPersonalIdCodeValid(person.getPersonalIdCode())) {
            throw new LoanValidationException(getExceptionMessage(Person.class, "Personal ID code",
                    false));
        }

        if (person.getPassword().length() < 6) {
            throw new LoanValidationException(getExceptionMessage(Person.class, "password",
                    false));
        }

        try {
            personService.findPersonByPersonalIdCode(person.getPersonalIdCode());
            throw new LoanValidationException(getExceptionMessage(Person.class, "Personal ID code",
                    true));
        } catch (PersonNotFoundException e) {
            log.info("Person with personal id {} not found", person.getPersonalIdCode());
        }


        if (person.getCreditModifier() != null) {
            try {
                creditModifierService.findCreditModifierById(person.getId());
            } catch (CreditModifierNotFoundException e) {
                throw new LoanValidationException(getExceptionMessage(Person.class, "credit modifier",
                        false));
            }
        }
    }

    // PRIVATE METHODS //
    private String getExceptionMessage(Class<?> clazz, String fieldName, boolean isAlreadyExists) {
        String className = LoanUtils.getStringOfClassName(clazz);

        return isAlreadyExists ? MessageFormat.format("{0} {1} already exists!", className, fieldName) :
                MessageFormat.format("Invalid {0} {1}", className, fieldName);
    }
}
