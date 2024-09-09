package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.dtos.LoanRequest;
import com.inbank.loanserver.exceptions.*;
import com.inbank.loanserver.models.CreditModifier;
import com.inbank.loanserver.models.KeyValueStore;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.models.Role;
import com.inbank.loanserver.services.*;
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

    @Autowired
    private RoleService roleService;

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

        if (creditModifier.getId() == null && !creditModifier.isActive()) {
            try {
                creditModifierService.findCreditModifierByName(creditModifier.getName().toUpperCase());
                throw new LoanValidationException(getExceptionMessage(CreditModifier.class, "name",
                        true));
            } catch (CreditModifierNotFoundException e) {
                log.info("Credit Modifier with name {} not found", creditModifier.getName());
            }
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

        if (keyValueStore.getId() == null && !keyValueStore.isActive()) {
            try {
                keyValueStoreService.findKeyValueStoreByKey(keyValueStore.getKey());
                throw new LoanValidationException(getExceptionMessage(KeyValueStore.class, "key",
                        true));
            } catch (KeyValueStoreNotFoundException e) {
                log.info("Key store with key {} not found", keyValueStore.getKey());
            }
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

        if (person.getRole() != null) {
            try {
                roleService.findRoleById(person.getRole().getId());
            } catch (RoleNotFoundException e) {
                throw new LoanValidationException(getExceptionMessage(Person.class, "role",
                        false));
            }
        }
    }

    @Override
    public void validateRole(Role role) throws LoanValidationException {
        if (role.getName() == null) {
            throw new LoanValidationException(getExceptionMessage(Role.class, "Role type",
                    false));
        }

        try {
            roleService.findRoleByName(role.getName());
            throw new LoanValidationException(getExceptionMessage(KeyValueStore.class, "Role type",
                    true));
        } catch (RoleNotFoundException e) {
            log.info("Role with name {} not found", role.getName());
        }
    }

    // PRIVATE METHODS //
    private String getExceptionMessage(Class<?> clazz, String fieldName, boolean isAlreadyExists) {
        String className = LoanUtils.getStringOfClassName(clazz);

        return isAlreadyExists ? MessageFormat.format("{0} {1} already exists!", className, fieldName) :
                MessageFormat.format("Invalid {0} {1}", className, fieldName);
    }
}
