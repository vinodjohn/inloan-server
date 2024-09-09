package com.inbank.loanserver.utils;

/**
 * Constant values used in this application
 *
 * @author vinodjohn
 * @created 31.08.2024
 */
public class Constants {
    public static class Data {
        public static final String DEFAULT_ITEMS_PER_PAGE = "10";
    }

    public static class Audit {
        public static final String DEFAULT_AUDITOR = "SYSTEM";
        public static final String ROLE_ADMIN = "ADMIN";
        public static final String ROLE_USER = "USER";
    }

    public static class KvStore {
        public static final String MINIMUM_LOAN_AMOUNT = "MINIMUM_LOAN_AMOUNT";
        public static final String MAXIMUM_LOAN_AMOUNT = "MAXIMUM_LOAN_AMOUNT";
        public static final String MINIMUM_LOAN_PERIOD = "MINIMUM_LOAN_PERIOD_MONTHS";
        public static final String MAXIMUM_LOAN_PERIOD = "MAXIMUM_LOAN_PERIOD_MONTHS";
        public static final String CREDIT_COEFFICIENT = "CREDIT_COEFFICIENT";
    }
}
