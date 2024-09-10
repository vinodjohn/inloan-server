package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.PersonNotFoundException;
import com.inbank.loanserver.exceptions.TokenRefreshException;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.models.TokenRefresh;
import com.inbank.loanserver.repositories.TokenRefreshRepository;
import com.inbank.loanserver.services.implementations.TokenRefreshServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for TokenRefreshService
 *
 * @author vinodjohn
 * @created 10.09.2024
 */
@ExtendWith(MockitoExtension.class)
public class TokenRefreshServiceTest {
    @Mock
    private TokenRefreshRepository tokenRefreshRepository;

    @Mock
    private PersonService personService;

    @InjectMocks
    private TokenRefreshServiceImpl tokenRefreshService;

    @Value("${inLoan.app.jwtRefreshExpirationSec}")
    private int tokenRefreshDuration = 3600;

    private TokenRefresh tokenRefresh;
    private UUID tokenRefreshId;
    private String token;
    private UUID personId;
    private Person person;

    @BeforeEach
    void setUp() {
        tokenRefreshId = UUID.randomUUID();
        token = UUID.randomUUID().toString();
        personId = UUID.randomUUID();
        person = new Person();
        person.setId(personId);
        tokenRefresh = new TokenRefresh();
        tokenRefresh.setId(tokenRefreshId);
        tokenRefresh.setToken(token);
        tokenRefresh.setEndTime(LocalDateTime.now().plusSeconds(tokenRefreshDuration));
        tokenRefresh.setPerson(person);
        tokenRefresh.setActive(true);
    }

    @Test
    void testCreateRefreshTokenWhenValidPersonThenTokenIsCreated() throws PersonNotFoundException {
        when(personService.findPersonById(personId)).thenReturn(person);
        when(tokenRefreshRepository.saveAndFlush(any(TokenRefresh.class))).thenReturn(tokenRefresh);

        TokenRefresh createdTokenRefresh = tokenRefreshService.createRefreshToken(personId);

        assertNotNull(createdTokenRefresh);
        assertEquals(person, createdTokenRefresh.getPerson());
        verify(personService, times(1)).findPersonById(personId);
        verify(tokenRefreshRepository, times(1)).saveAndFlush(any(TokenRefresh.class));
    }

    @Test
    void testFindTokenRefreshByIdWhenValidIdThenTokenIsReturned() {
        when(tokenRefreshRepository.findById(tokenRefreshId)).thenReturn(Optional.of(tokenRefresh));
        TokenRefresh foundTokenRefresh = tokenRefreshService.findTokenRefreshById(tokenRefreshId);
        assertEquals(tokenRefresh, foundTokenRefresh);
        verify(tokenRefreshRepository, times(1)).findById(tokenRefreshId);
    }

    @Test
    void testFindTokenRefreshByIdWhenInvalidIdThenExceptionIsThrown() {
        when(tokenRefreshRepository.findById(tokenRefreshId)).thenReturn(Optional.empty());
        assertThrows(TokenRefreshException.class, () -> tokenRefreshService.findTokenRefreshById(tokenRefreshId));
        verify(tokenRefreshRepository, times(1)).findById(tokenRefreshId);
    }

    @Test
    void testFindTokenRefreshByTokenWhenValidTokenThenTokenIsReturned() {
        when(tokenRefreshRepository.findByToken(token)).thenReturn(Optional.of(tokenRefresh));
        TokenRefresh foundTokenRefresh = tokenRefreshService.findTokenRefreshByToken(token);
        assertEquals(tokenRefresh, foundTokenRefresh);
        verify(tokenRefreshRepository, times(1)).findByToken(token);
    }

    @Test
    void testFindTokenRefreshByTokenWhenInvalidTokenThenExceptionIsThrown() {
        when(tokenRefreshRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(TokenRefreshException.class, () -> tokenRefreshService.findTokenRefreshByToken(token));
        verify(tokenRefreshRepository, times(1)).findByToken(token);
    }

    @Test
    void testVerifyTokenExpiryWhenTokenIsValidThenTokenIsReturned() {
        TokenRefresh verifiedTokenRefresh = tokenRefreshService.verifyTokenExpiry(tokenRefresh);
        assertEquals(tokenRefresh, verifiedTokenRefresh);
    }

    @Test
    void testDeleteTokenRefreshByIdWhenValidIdThenTokenIsDeleted() {
        when(tokenRefreshRepository.findById(tokenRefreshId)).thenReturn(Optional.of(tokenRefresh));

        tokenRefreshService.deleteTokenRefreshById(tokenRefreshId);

        assertFalse(tokenRefresh.isActive());
        verify(tokenRefreshRepository, times(1)).findById(tokenRefreshId);
        verify(tokenRefreshRepository, times(1)).saveAndFlush(tokenRefresh);
    }

    @Test
    void testDeleteTokenRefreshByPersonIdWhenValidPersonIdThenTokenIsDeleted() {
        when(tokenRefreshRepository.findLatestActiveTokenByPerson(personId)).thenReturn(Optional.of(tokenRefresh));

        tokenRefreshService.deleteTokenRefreshByPersonId(personId);

        assertFalse(tokenRefresh.isActive());
        verify(tokenRefreshRepository, times(1)).findLatestActiveTokenByPerson(personId);
        verify(tokenRefreshRepository, times(1)).saveAndFlush(tokenRefresh);
    }

    @Test
    void testDeleteTokenRefreshByPersonIdWhenInvalidPersonIdThenExceptionIsThrown() {
        when(tokenRefreshRepository.findLatestActiveTokenByPerson(personId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> tokenRefreshService.deleteTokenRefreshByPersonId(personId));
        verify(tokenRefreshRepository, times(1)).findLatestActiveTokenByPerson(personId);
    }
}