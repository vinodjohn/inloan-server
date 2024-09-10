package com.inbank.loanserver.handlers;

import com.inbank.loanserver.exceptions.*;
import com.inbank.loanserver.handlers.exception.CustomExceptionHandler;
import com.inbank.loanserver.handlers.exception.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomExceptionHandlerTest {

    private static final UUID TEST_UUID = UUID.fromString("580587bb-97ff-4008-b29f-acd232360645");
    private static final String DEFAULT_MESSAGE = "Server error";

    private static final Exception GENERIC_EXCEPTION = new Exception(DEFAULT_MESSAGE);
    private static final KeyValueStoreNotFoundException KEY_VALUE_STORE_NOT_FOUND_EXCEPTION =
            new KeyValueStoreNotFoundException("MINIMUM_LOAN_AMOUNT");
    private static final LoanApplicationNotFoundException LOAN_APPLICATION_NOT_FOUND_EXCEPTION =
            new LoanApplicationNotFoundException(TEST_UUID);
    private static final LoanOfferNotFoundException LOAN_OFFER_NOT_FOUND_EXCEPTION =
            new LoanOfferNotFoundException(TEST_UUID);
    private static final LoanValidationException LOAN_VALIDATION_EXCEPTION = new LoanValidationException("Loan " +
            "validation failed");
    private static final RoleNotFoundException ROLE_NOT_FOUND_EXCEPTION = new RoleNotFoundException("ADMIN");
    private static final TokenRefreshException TOKEN_REFRESH_EXCEPTION = new TokenRefreshException(TEST_UUID);

    @InjectMocks
    private CustomExceptionHandler customExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Test
    void testHandleAllExceptions() {
        ResponseEntity<Object> response = customExceptionHandler.handleAllExceptions(GENERIC_EXCEPTION);
        assertErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Server Error!", DEFAULT_MESSAGE);
    }

    @Test
    void testHandleKeyValueStoreNotFoundException() {
        ResponseEntity<Object> response =
                customExceptionHandler.handleKeyValueStoreNotFoundException(KEY_VALUE_STORE_NOT_FOUND_EXCEPTION);
        assertErrorResponse(response, HttpStatus.NOT_FOUND, "Key Value Store not found!", "KeyValue not found! (Key: " +
                "MINIMUM_LOAN_AMOUNT)");
    }

    @Test
    void testHandleLoanApplicationNotFoundException() {
        ResponseEntity<Object> response =
                customExceptionHandler.handleLoanApplicationNotFoundException(LOAN_APPLICATION_NOT_FOUND_EXCEPTION);
        assertErrorResponse(response, HttpStatus.NOT_FOUND, "Loan Application not found!", MessageFormat.format("Loan" +
                " Application not found! (ID: {0})", TEST_UUID));
    }


    @Test
    void testHandleLoanOfferNotFoundException() {
        ResponseEntity<Object> response =
                customExceptionHandler.handleLoanOfferNotFoundException(LOAN_OFFER_NOT_FOUND_EXCEPTION);
        assertErrorResponse(response, HttpStatus.NOT_FOUND, "Loan Offer not found!", MessageFormat.format("Loan " +
                "Offer not found! (ID: {0})", TEST_UUID));
    }

    @Test
    void testHandleLoanValidationException() {
        ResponseEntity<Object> response =
                customExceptionHandler.handleLoanValidationException(LOAN_VALIDATION_EXCEPTION);
        assertErrorResponse(response, HttpStatus.NOT_FOUND, "Loan Validation Exception not found!", "Loan validation " +
                "failed");
    }

    @Test
    void testHandleRoleNotFoundException() {
        ResponseEntity<Object> response = customExceptionHandler.handleRoleNotFoundException(ROLE_NOT_FOUND_EXCEPTION);
        assertErrorResponse(response, HttpStatus.NOT_FOUND, "Role not found!", "Role not found! (Name: ADMIN)");
    }

    @Test
    void testHandleTokenRefreshException() {
        when(webRequest.getDescription(false)).thenReturn("some detail");
        ResponseEntity<Object> response = customExceptionHandler.handleTokenRefreshException(TOKEN_REFRESH_EXCEPTION,
                webRequest);
        assertErrorResponse(response, HttpStatus.FORBIDDEN, MessageFormat.format("Token Refresh not found! (ID: {0})"
                , TEST_UUID), "some detail");
    }

    private void assertErrorResponse(ResponseEntity<Object> response, HttpStatus expectedStatus,
                                     String expectedMessage, String expectedDetail) {
        assertEquals(expectedStatus, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(expectedMessage, Objects.requireNonNull(errorResponse).getMessage());
        assertEquals(1, errorResponse.getDetails().size());
        assertEquals(expectedDetail, errorResponse.getDetails().getFirst());
    }
}