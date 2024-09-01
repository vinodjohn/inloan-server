package com.inbank.loanserver.handlers.exception;

import com.inbank.loanserver.exceptions.*;
import com.inbank.loanserver.models.*;
import com.inbank.loanserver.utils.LoanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler for exceptions
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Server Error!", details);
        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(CreditModifierNotFoundException.class)
    public final ResponseEntity<Object> handleCreditModifierNotFoundException(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex.getLocalizedMessage(), CreditModifier.class),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(KeyValueStoreNotFoundException.class)
    public final ResponseEntity<Object> handleKeyValueStoreNotFoundException(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex.getLocalizedMessage(), KeyValueStore.class),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoanApplicationNotFoundException.class)
    public final ResponseEntity<Object> handleLoanApplicationNotFoundException(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex.getLocalizedMessage(), LoanApplication.class),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoanOfferNotFoundException.class)
    public final ResponseEntity<Object> handleLoanOfferNotFoundException(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex.getLocalizedMessage(), LoanOffer.class),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoanValidationException.class)
    public final ResponseEntity<Object> handleLoanValidationException(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex.getLocalizedMessage(), LoanValidationException.class),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public final ResponseEntity<Object> handlePersonNotFoundException(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex.getLocalizedMessage(), Person.class),
                HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }

        ErrorResponse error = new ErrorResponse("Validation Failed!", details);
        return ResponseEntity.badRequest().body(error);
    }

    // PRIVATE METHODS //
    private ErrorResponse getErrorResponse(String description, Object object) {
        String className = LoanUtils.getStringOfClassName(object);
        List<String> details = new ArrayList<>();
        details.add(description);

        return new ErrorResponse(MessageFormat.format("{0} not found!", className), details);
    }
}
