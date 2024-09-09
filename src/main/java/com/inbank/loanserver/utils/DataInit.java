package com.inbank.loanserver.utils;

import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.exceptions.RoleNotFoundException;
import com.inbank.loanserver.models.*;
import com.inbank.loanserver.services.CreditModifierService;
import com.inbank.loanserver.services.KeyValueStoreService;
import com.inbank.loanserver.services.PersonService;
import com.inbank.loanserver.services.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.inbank.loanserver.utils.Constants.Audit.ROLE_ADMIN;
import static com.inbank.loanserver.utils.Constants.Audit.ROLE_USER;
import static com.inbank.loanserver.utils.Constants.KvStore.*;

/**
 * Component to initialize data on startup
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Slf4j
@Component
public class DataInit {
    private static final String CREDIT_MODIFIER_DEBT = "debt";
    private static final String CREDIT_MODIFIER_SEGMENT1 = "segment1";
    private static final String CREDIT_MODIFIER_SEGMENT2 = "segment2";
    private static final String CREDIT_MODIFIER_SEGMENT3 = "segment3";

    @Autowired
    private CreditModifierService creditModifierService;

    @Autowired
    private KeyValueStoreService keyValueStoreService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PersonService personService;

    @PostConstruct
    public void init() {
        log.info("Data init started");

        initCreditModifier();
        initKeyValueStore();
        initRole();
        initPerson();

        log.info("Data init finished");
    }

    // PRIVATE METHODS //
    private void initCreditModifier() {
        log.info("Data init credit modifier started");

        CreditModifier creditModifier = new CreditModifier();
        creditModifier.setName(CREDIT_MODIFIER_DEBT);
        creditModifier.setValue(0);
        creditModifierService.createCreditModifier(creditModifier);

        creditModifier = new CreditModifier();
        creditModifier.setName(CREDIT_MODIFIER_SEGMENT1);
        creditModifier.setValue(100);
        creditModifierService.createCreditModifier(creditModifier);

        creditModifier = new CreditModifier();
        creditModifier.setName(CREDIT_MODIFIER_SEGMENT2);
        creditModifier.setValue(300);
        creditModifierService.createCreditModifier(creditModifier);

        creditModifier = new CreditModifier();
        creditModifier.setName(CREDIT_MODIFIER_SEGMENT3);
        creditModifier.setValue(1000);
        creditModifierService.createCreditModifier(creditModifier);

        log.info("Data init credit modifier finished");
    }

    private void initKeyValueStore() {
        log.info("Data init key store started");

        KeyValueStore keyValueStore = new KeyValueStore();
        keyValueStore.setKey(MINIMUM_LOAN_AMOUNT);
        keyValueStore.setValue(2000);
        keyValueStoreService.createKeyValueStore(keyValueStore);

        keyValueStore = new KeyValueStore();
        keyValueStore.setKey(MAXIMUM_LOAN_AMOUNT);
        keyValueStore.setValue(10000);
        keyValueStoreService.createKeyValueStore(keyValueStore);

        keyValueStore = new KeyValueStore();
        keyValueStore.setKey(MINIMUM_LOAN_PERIOD);
        keyValueStore.setValue(12);
        keyValueStoreService.createKeyValueStore(keyValueStore);

        keyValueStore = new KeyValueStore();
        keyValueStore.setKey(MAXIMUM_LOAN_PERIOD);
        keyValueStore.setValue(60);
        keyValueStoreService.createKeyValueStore(keyValueStore);

        keyValueStore = new KeyValueStore();
        keyValueStore.setKey(CREDIT_COEFFICIENT);
        keyValueStore.setValue(1);
        keyValueStoreService.createKeyValueStore(keyValueStore);

        keyValueStore = new KeyValueStore();
        keyValueStore.setKey("BASE_INTEREST_RATE");
        keyValueStore.setValue(5);
        keyValueStoreService.createKeyValueStore(keyValueStore);

        log.info("Data init key store finished");
    }

    private void initRole() {
        log.info("Data init role started");

        Role role = new Role();
        role.setName(ROLE_ADMIN);
        roleService.createRole(role);

        role = new Role();
        role.setName(ROLE_USER);
        roleService.createRole(role);

        log.info("Data init role finished");
    }

    private void initPerson() {
        log.info("Data init person started");

        try {
            Person person = new Person();
            CreditModifier creditModifierSegment3 =
                    creditModifierService.findCreditModifierByName(CREDIT_MODIFIER_SEGMENT3);
            Role roleAdmin = roleService.findRoleByName(ROLE_ADMIN);
            person.setFirstName("Falcon");
            person.setLastName("Ameri");
            person.setPersonalIdCode("39403160272");
            person.setPassword("123456");
            person.setCreditModifier(creditModifierSegment3);
            person.setRole(roleAdmin);
            personService.createPerson(person);

            person = new Person();
            CreditModifier creditModifierDebt = creditModifierService.findCreditModifierByName(CREDIT_MODIFIER_DEBT);
            Role roleUser = roleService.findRoleByName(ROLE_USER);
            person.setFirstName("Thor");
            person.setLastName("Thunder");
            person.setPersonalIdCode("49002010965");
            person.setPassword("123456");
            person.setCreditModifier(creditModifierDebt);
            person.setRole(roleUser);
            personService.createPerson(person);

            person = new Person();
            CreditModifier creditModifierSegment1 =
                    creditModifierService.findCreditModifierByName(CREDIT_MODIFIER_SEGMENT1);
            person.setFirstName("Tony");
            person.setLastName("Stark");
            person.setPersonalIdCode("49002010976");
            person.setPassword("123456");
            person.setCreditModifier(creditModifierSegment1);
            person.setRole(roleUser);
            personService.createPerson(person);

            person = new Person();
            CreditModifier creditModifierSegment2 =
                    creditModifierService.findCreditModifierByName(CREDIT_MODIFIER_SEGMENT2);
            person.setFirstName("Natasha");
            person.setLastName("Romanof");
            person.setPersonalIdCode("49002010987");
            person.setPassword("123456");
            person.setCreditModifier(creditModifierSegment2);
            person.setRole(roleUser);
            personService.createPerson(person);

            person = new Person();
            person.setFirstName("Bruce");
            person.setLastName("Banner");
            person.setPersonalIdCode("49002010998");
            person.setPassword("123456");
            person.setCreditModifier(creditModifierSegment3);
            person.setRole(roleUser);
            personService.createPerson(person);

            log.info("Data init person finished");
        } catch (CreditModifierNotFoundException | RoleNotFoundException e) {
            log.error("Error Person Data Init!");
            log.info(e.getLocalizedMessage());
        }
    }

}
