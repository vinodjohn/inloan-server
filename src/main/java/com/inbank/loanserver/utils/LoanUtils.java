package com.inbank.loanserver.utils;

import org.springframework.data.domain.Sort;
import org.springframework.util.ClassUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * A helper class to provide common functionalities for this app
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
public class LoanUtils {
    public static Sort getSortOfColumn(String sort, String order) {
        return order.equalsIgnoreCase("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending();
    }

    public static String getStringOfClassName(Class<?> clazz) {
        String className = ClassUtils.getShortName(clazz.getSimpleName()).concat("'s");
        className = Pattern.compile("(?<=[a-z])(?=[A-Z])")
                .matcher(className)
                .replaceAll(" ");

        return className;
    }

    public static boolean isPersonalIdCodeValid(String idNumber) {
        if (idNumber == null || idNumber.length() != 11 || !idNumber.matches("\\d{11}")) {
            return false;
        }

        int century = idNumber.charAt(0) - '0';

        if (century < 1 || century > 6) {
            return false;
        }

        String birthDate = idNumber.substring(1, 7);

        if (!isBirthDateValid(birthDate, century)) {
            return false;
        }

        return checkChecksum(idNumber);
    }

    // PRIVATE METHODS //
    private static boolean isBirthDateValid(String birthDate, int century) {
        int year = Integer.parseInt(birthDate.substring(0, 2)),
                month = Integer.parseInt(birthDate.substring(2, 4)),
                day = Integer.parseInt(birthDate.substring(4, 6));

        year += switch (century) {
            case 1, 2 -> 1800;
            case 3, 4 -> 1900;
            case 5, 6 -> 2000;
            default -> throw new IllegalArgumentException("Invalid century digit");
        };

        try {
            LocalDate _ = LocalDate.of(year, month, day);
            return true;
        } catch (DateTimeParseException | IllegalArgumentException _) {
            return false;
        }
    }

    private static boolean checkChecksum(String idNumber) {
        int[] firstWeights = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1},
                secondWeights = {3, 4, 5, 6, 7, 8, 9, 1, 2, 3};
        int expectedChecksum = idNumber.charAt(10) - '0',
                totalFirstWeight = IntStream.range(0, 10)
                        .map(i -> (idNumber.charAt(i) - '0') * firstWeights[i])
                        .sum(),
                remainder = totalFirstWeight % 11;

        if (remainder == 10) {
            int totalSecondWeight = IntStream.range(0, 10)
                    .map(i -> (idNumber.charAt(i) - '0') * secondWeights[i])
                    .sum();

            remainder = totalSecondWeight % 11;

            if (remainder == 10) {
                remainder = 0;
            }

            return remainder == expectedChecksum;
        }

        return false;
    }
}
