package com.inbank.loanserver.utils;

import org.springframework.data.domain.Sort;

/**
 * A helper class to provide common functionalities for this app
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
public class LoanUtils {
    public static Sort getSortOfColumn(String sort, String order) {
        if (order.equalsIgnoreCase("asc")) {
            return Sort.by(sort).ascending();
        } else {
            return Sort.by(sort).descending();
        }
    }
}
