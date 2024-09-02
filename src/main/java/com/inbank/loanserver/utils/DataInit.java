package com.inbank.loanserver.utils;

import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.models.CreditModifier;
import com.inbank.loanserver.models.KeyValueStore;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.services.CreditModifierService;
import com.inbank.loanserver.services.KeyValueStoreService;
import com.inbank.loanserver.services.PersonService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private PersonService personService;

    @PostConstruct
    public void init() {
        log.info("Data init started");

        initCreditModifier();
        initKeyValueStore();
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

        log.info("Data init key store finished");
    }

    private void initPerson() {
        log.info("Data init person started");

        try {
            Person person = new Person();
            CreditModifier creditModifierDebt = creditModifierService.findCreditModifierByName(CREDIT_MODIFIER_DEBT);
            person.setFirstName("Thor");
            person.setLastName("Thunder");
            person.setPersonalIdCode("49002010965");
            person.setPassword("123456");
            person.setCreditModifier(creditModifierDebt);
            personService.createPerson(person);

            person = new Person();
            CreditModifier creditModifierSegment1 =
                    creditModifierService.findCreditModifierByName(CREDIT_MODIFIER_SEGMENT1);
            person.setFirstName("Tony");
            person.setLastName("Stark");
            person.setPersonalIdCode("49002010976");
            person.setPassword("123456");
            person.setCreditModifier(creditModifierSegment1);
            personService.createPerson(person);

            person = new Person();
            CreditModifier creditModifierSegment2 =
                    creditModifierService.findCreditModifierByName(CREDIT_MODIFIER_SEGMENT2);
            person.setFirstName("Natasha");
            person.setLastName("Romanof");
            person.setPersonalIdCode("49002010987");
            person.setPassword("123456");
            person.setCreditModifier(creditModifierSegment2);
            personService.createPerson(person);

            person = new Person();
            CreditModifier creditModifierSegment3 =
                    creditModifierService.findCreditModifierByName(CREDIT_MODIFIER_SEGMENT2);
            person.setFirstName("Bruce");
            person.setLastName("Banner");
            person.setPersonalIdCode("49002010998");
            person.setPassword("123456");
            person.setCreditModifier(creditModifierSegment3);
            personService.createPerson(person);

            log.info("Data init person finished");
        } catch (CreditModifierNotFoundException e) {
            log.error("Error Person Data Init!");
            log.info(e.getLocalizedMessage());
        }
    }

}
