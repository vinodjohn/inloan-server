package com.inbank.loanserver.utils;

import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.exceptions.RoleNotFoundException;
import com.inbank.loanserver.models.CreditModifier;
import com.inbank.loanserver.models.KeyValueStore;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.models.Role;
import com.inbank.loanserver.services.CreditModifierService;
import com.inbank.loanserver.services.KeyValueStoreService;
import com.inbank.loanserver.services.PersonService;
import com.inbank.loanserver.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for Data init
 *
 * @author vinodjohn
 * @created 10.09.2024
 */
public class DataInitTest {
    private static final int CREDIT_MODIFIER_CREATE_TIMES = 4;
    private static final int KEY_VALUE_STORE_CREATE_TIMES = 6;
    private static final int ROLE_CREATE_TIMES = 2;
    private static final int PERSON_CREATE_TIMES = 5;

    @InjectMocks
    private DataInit dataInit;

    @Mock
    private CreditModifierService creditModifierService;

    @Mock
    private KeyValueStoreService keyValueStoreService;

    @Mock
    private RoleService roleService;

    @Mock
    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInit() throws CreditModifierNotFoundException, RoleNotFoundException {
        mockCreateMethods();
        mockFindMethods();
        dataInit.init();
        verify(creditModifierService, times(CREDIT_MODIFIER_CREATE_TIMES)).createCreditModifier(any(CreditModifier.class));
        verify(keyValueStoreService, times(KEY_VALUE_STORE_CREATE_TIMES)).createKeyValueStore(any(KeyValueStore.class));
        verify(roleService, times(ROLE_CREATE_TIMES)).createRole(any(Role.class));
        verify(personService, times(PERSON_CREATE_TIMES)).createPerson(any(Person.class));
    }

    @Test
    void shouldInitializeCreditModifier() {
        dataInit.initCreditModifier();
        verify(creditModifierService, times(CREDIT_MODIFIER_CREATE_TIMES)).createCreditModifier(any(CreditModifier.class));
    }

    @Test
    void shouldInitializeKeyValueStore() {
        dataInit.initKeyValueStore();
        verify(keyValueStoreService, times(KEY_VALUE_STORE_CREATE_TIMES)).createKeyValueStore(any(KeyValueStore.class));
    }

    @Test
    void shouldInitializeRole() {
        dataInit.initRole();
        verify(roleService, times(ROLE_CREATE_TIMES)).createRole(any(Role.class));
    }

    @Test
    void shouldInitializePerson() throws CreditModifierNotFoundException, RoleNotFoundException {
        mockFindMethods();
        dataInit.initPerson();
        verify(personService, times(PERSON_CREATE_TIMES)).createPerson(any(Person.class));
    }

    @Test
    void shouldHandleCreditModifierNotFoundException() throws CreditModifierNotFoundException, RoleNotFoundException {
        when(creditModifierService.findCreditModifierByName(anyString())).thenThrow(new CreditModifierNotFoundException(UUID.randomUUID()));
        dataInit.initPerson();
        verify(personService, times(0)).createPerson(any(Person.class));
    }

    @Test
    void shouldHandleRoleNotFoundException() throws CreditModifierNotFoundException, RoleNotFoundException {
        when(roleService.findRoleByName(anyString())).thenThrow(new RoleNotFoundException(UUID.randomUUID()));
        dataInit.initPerson();
        verify(personService, times(0)).createPerson(any(Person.class));
    }

    private void mockCreateMethods() throws RoleNotFoundException, CreditModifierNotFoundException {
        when(creditModifierService.createCreditModifier(any(CreditModifier.class))).thenReturn(new CreditModifier());
        when(keyValueStoreService.createKeyValueStore(any(KeyValueStore.class))).thenReturn(new KeyValueStore());
        when(roleService.createRole(any(Role.class))).thenReturn(new Role());
        when(personService.createPerson(any(Person.class))).thenReturn(new Person());
    }

    private void mockFindMethods() throws CreditModifierNotFoundException, RoleNotFoundException {
        when(creditModifierService.findCreditModifierByName(anyString())).thenReturn(new CreditModifier());
        when(roleService.findRoleByName(anyString())).thenReturn(new Role());
    }
}