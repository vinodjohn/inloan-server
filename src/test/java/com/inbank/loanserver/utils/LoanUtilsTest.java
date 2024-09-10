package com.inbank.loanserver.utils;


import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for LoanUtils
 *
 * @author vinodjohn
 * @created 10.09.2024
 */
public class LoanUtilsTest {
    private static final String SORT_NAME = "name";
    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private static final String VALID_PERSONAL_ID = "39912319999";
    private static final String INVALID_LENGTH_PERSONAL_ID = "1234567890";
    private static final String INVALID_CHARACTERS_PERSONAL_ID = "1234567890A";
    private static final String INVALID_CENTURY_PERSONAL_ID = "79912319999";
    private static final String INVALID_CHECKSUM_PERSONAL_ID = "39102059998";
    private static final String VALID_BIRTH_DATE = "991231";
    private static final int VALID_CENTURY = 3;

    @Test
    void shouldReturnAscendingSortForGivenColumn() {
        Sort sort = LoanUtils.getSortOfColumn(SORT_NAME, ASC);
        assertNotNull(sort);
        assertEquals(Sort.by(SORT_NAME).ascending(), sort);
    }

    @Test
    void shouldReturnDescendingSortForGivenColumn() {
        Sort sort = LoanUtils.getSortOfColumn(SORT_NAME, DESC);
        assertNotNull(sort);
        assertEquals(Sort.by(SORT_NAME).descending(), sort);
    }

    @Test
    void shouldReturnFormattedClassName() {
        String className = LoanUtils.getStringOfClassName(LoanUtils.class);
        assertEquals("Loan Utils", className);
    }

    @Test
    void shouldReturnFalseForValidPersonalIdCode() {
        assertFalse(LoanUtils.isPersonalIdCodeValid(VALID_PERSONAL_ID));
    }

    @Test
    void shouldReturnFalseForInvalidLengthPersonalIdCode() {
        assertFalse(LoanUtils.isPersonalIdCodeValid(INVALID_LENGTH_PERSONAL_ID));
    }

    @Test
    void shouldReturnFalseForInvalidCharactersInPersonalIdCode() {
        assertFalse(LoanUtils.isPersonalIdCodeValid(INVALID_CHARACTERS_PERSONAL_ID));
    }

    @Test
    void shouldReturnFalseForInvalidCenturyInPersonalIdCode() {
        assertFalse(LoanUtils.isPersonalIdCodeValid(INVALID_CENTURY_PERSONAL_ID));
    }

    @Test
    void shouldReturnFalseForInvalidBirthDateInPersonalIdCode() {
        assertFalse(LoanUtils.isPersonalIdCodeValid(VALID_PERSONAL_ID));
    }

    @Test
    void shouldReturnFalseForInvalidChecksumInPersonalIdCode() {
        assertFalse(LoanUtils.isPersonalIdCodeValid(INVALID_CHECKSUM_PERSONAL_ID));
    }

    @Test
    void shouldReturnTrueForValidBirthDate() {
        assertTrue(LoanUtils.isBirthDateValid(VALID_BIRTH_DATE, VALID_CENTURY));
    }

    @Test
    void shouldReturnTrueForInvalidBirthDate() {
        assertTrue(LoanUtils.isBirthDateValid(INVALID_CHECKSUM_PERSONAL_ID, VALID_CENTURY));
    }

    @Test
    void shouldReturnFalseForValidChecksum() {
        assertFalse(LoanUtils.checkChecksum(VALID_PERSONAL_ID));
    }

    @Test
    void shouldReturnFalseForInvalidChecksum() {
        assertFalse(LoanUtils.checkChecksum(INVALID_CHECKSUM_PERSONAL_ID));
    }
}