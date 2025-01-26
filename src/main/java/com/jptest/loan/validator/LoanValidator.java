package com.jptest.loan.validator;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Arrays;

/**
 * Component for validating loan input parameters.
 * This class provides methods to validate year format, year comparison with current year,
 * loan amount, vehicle type, vehicle condition, and loan tenor.
 */
@Component
public class LoanValidator {

    /**
     * Validates if the input year is a four-digit year.
     *
     * @param year The year to validate.
     * @return true if the year is not a four-digit year, false otherwise.
     */
    public boolean isValidYearFourDigit(int year) {
        return !String.valueOf(year).matches("\\d{4}");
    }

    /**
     * Validates if the input year is not in the future compared to the current year.
     *
     * @param inputYear The year to validate.
     * @return true if the input year is in the future, false otherwise.
     */
    public boolean isValidYearCompareWithCurrentYear(int inputYear) {
        int currentYear = Year.now().getValue();
        return inputYear > currentYear;
    }

    /**
     * Validates if the loan amount is within the acceptable range (0 < loanAmount <= 1,000,000,000).
     *
     * @param loanAmount The loan amount to validate.
     * @return true if the loan amount is not valid (less than or equal to zero, or greater than 1 billion), false otherwise.
     */
    public boolean isValidLoanAmount(double loanAmount) {
        try {
            BigDecimal amount = new BigDecimal(loanAmount);
            return amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(new BigDecimal(1_000_000_000)) > 0;
        } catch (NumberFormatException e) {
            return true; // Consider invalid if parsing fails
        }
    }

    /**
     * Validates if the vehicle type is one of the allowed types ("car", "motorcycle").
     *
     * @param vehicleType The vehicle type to validate.
     * @return true if the vehicle type is not valid, false otherwise.
     */
    public boolean isValidVehicleType(String vehicleType) {
        return !Arrays.asList("car", "motorcycle").contains(vehicleType.toLowerCase());
    }

    /**
     * Validates if the vehicle condition is one of the allowed conditions ("new", "old").
     *
     * @param vehicleCondition The vehicle condition to validate.
     * @return true if the vehicle condition is not valid, false otherwise.
     */
    public boolean isValidVehicleCondition(String vehicleCondition) {
        return !Arrays.asList("new", "old").contains(vehicleCondition.toLowerCase());
    }

    /**
     * Validates if the vehicle condition is one of the allowed conditions ("new", "old").
     *
     * @param vehicleCondition The vehicle condition to validate.
     * @return true if the vehicle condition is not valid, false otherwise.
     */
    public boolean isValidVehicleIfNewCondition(String vehicleCondition, int vehicleYear) {
        int currentYear = Year.now().getValue();
        return "new".equalsIgnoreCase(vehicleCondition) && vehicleYear < currentYear - 1;
    }


    /**
     * Validates if the loan tenor is within the acceptable range (1 to 6 years).
     *
     * @param loanTenor The loan tenor to validate.
     * @return true if the loan tenor is not valid (less than 1 or greater than 6), false otherwise.
     */
    public boolean isValidLoanTenor(int loanTenor) {
        return loanTenor < 1 || loanTenor > 6;
    }
}
