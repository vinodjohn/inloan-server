package com.inbank.loanserver.handlers;

import com.inbank.loanserver.configurations.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static com.inbank.loanserver.utils.Constants.Audit.DEFAULT_AUDITOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for AuditorAwareHandler
 *
 * @author vinodjohn
 * @created 10.09.2024
 */
@ExtendWith(MockitoExtension.class)
public class AuditorAwareHandlerTest {

    @InjectMocks
    private AuditorAwareHandler auditorAwareHandler;

    private SecurityContext mockSecurityContext;
    private Authentication mockAuthentication;
    private CustomUserDetails mockUserDetails;

    @BeforeEach
    void setUp() {
        mockSecurityContext = mock(SecurityContext.class);
        mockAuthentication = mock(Authentication.class);
        mockUserDetails = mock(CustomUserDetails.class);
    }

    @Test
    void testGetCurrentAuditorWhenAuthenticationIsNullThenReturnDefaultAuditor() {
        withMockedSecurityContext(() -> {
            when(mockSecurityContext.getAuthentication()).thenReturn(null);

            Optional<String> auditor = auditorAwareHandler.getCurrentAuditor();

            assertEquals(Optional.of(DEFAULT_AUDITOR), auditor);
        });
    }

    @Test
    void testGetCurrentAuditorWhenAuthenticationIsNotAuthenticatedThenReturnDefaultAuditor() {
        withMockedSecurityContext(() -> {
            when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
            when(mockAuthentication.isAuthenticated()).thenReturn(false);

            Optional<String> auditor = auditorAwareHandler.getCurrentAuditor();

            assertEquals(Optional.of(DEFAULT_AUDITOR), auditor);
        });
    }

    @Test
    void testGetCurrentAuditorWhenAuthenticationIsAnonymousUserThenReturnDefaultAuditor() {
        withMockedSecurityContext(() -> {
            when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
            when(mockAuthentication.isAuthenticated()).thenReturn(true);
            when(mockAuthentication.getPrincipal()).thenReturn("anonymousUser");

            Optional<String> auditor = auditorAwareHandler.getCurrentAuditor();

            assertEquals(Optional.of(DEFAULT_AUDITOR), auditor);
        });
    }

    @Test
    void testGetCurrentAuditorWhenAuthenticationIsValidThenReturnUsername() {
        String testUsername = "testUser";
        withMockedSecurityContext(() -> {
            when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
            when(mockAuthentication.isAuthenticated()).thenReturn(true);
            when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
            when(mockUserDetails.getUsername()).thenReturn(testUsername);

            Optional<String> auditor = auditorAwareHandler.getCurrentAuditor();

            assertEquals(Optional.of(testUsername), auditor);
        });
    }

    private void withMockedSecurityContext(Runnable testCode) {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder =
                     mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
            testCode.run();
        }
    }
}