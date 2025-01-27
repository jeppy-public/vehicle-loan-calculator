package com.jptest.loan.processor;

import com.jptest.loan.dto.MonthlyInstallmentRatePair;
import com.jptest.loan.service.LoanCalculatorService;

import com.jptest.loan.dto.MonthlyInstallmentRatePair;
import com.jptest.loan.service.LoanCalculatorService;

import java.util.List;

/**
 * {@code BaseProcessor} class serves as a foundation for loan processing operations.
 * <p>
 *     It is designed to be extended by classes that handle specific types of loan input processing
 *     (e.g., manual input, file input). This base class encapsulates common functionalities
 *     related to loan calculation and installment printing, leveraging the {@link LoanCalculatorService}.
 * </p>
 */
public class BaseProcessor {

    private final LoanCalculatorService loanCalculatorService;

    /**
     * Constructor for {@code BaseProcessor}.
     * <p>
     *     Initializes the base processor with a {@link LoanCalculatorService} instance.
     *     This service is used by implementing classes to perform the actual loan calculations.
     * </p>
     *
     * @param loanCalculatorService Service responsible for performing loan calculations.
     */
    public BaseProcessor(LoanCalculatorService loanCalculatorService) {
        this.loanCalculatorService = loanCalculatorService;
    }

    /**
     * Calculates and prints the monthly installment for each year of the loan tenor.
     * <p>
     *     This method takes loan parameters, uses {@link LoanCalculatorService} to calculate
     *     monthly installments for each year of the loan, and then prints these installments
     *     to the console, formatted with appropriate currency and interest rate display.
     * </p>
     *
     * @param vehicleType Type of vehicle (e.g., car, motorcycle).
     * @param vehicleCondition Condition of vehicle (new/old).
     * @param vehicleYear Year of vehicle manufacture.
     * @param loanAmount Total loan amount.
     * @param loanTenor Loan tenor in years.
     * @param downPayment Down payment amount.
     */
    public void calculateAndPrintInstallment(String vehicleType, String vehicleCondition, int vehicleYear, double loanAmount, int loanTenor, double downPayment) {
        try {
            // Calculate monthly installments using LoanCalculatorService
            List<MonthlyInstallmentRatePair> monthlyInstallment = loanCalculatorService.calculateMonthlyInstallment(
                    vehicleType, vehicleCondition, vehicleYear, loanAmount, loanTenor, downPayment
            );
            int year = 1; // Initialize year counter

            // Iterate through the calculated installments and print each year's installment details
            for(MonthlyInstallmentRatePair monthlyPair: monthlyInstallment){
                System.out.printf("\n%s year with Monthly installment: Rp %,.2f, Interest rate: %.1f%%%n", getOrdinal(year++), monthlyPair.amount(), monthlyPair.rate());
            }
            System.out.print("\n"); // Print a newline for better formatting

        } catch (IllegalArgumentException e) {
            // Catch and display any IllegalArgumentExceptions thrown during calculation
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Helper method to get the ordinal string representation of a year number.
     * <p>
     *     Converts a given integer to its ordinal form (e.g., 1 becomes "1st", 2 becomes "2nd", etc.).
     *     This is used for displaying user-friendly year numbers in output messages.
     * </p>
     *
     * @param n Year number for which to get the ordinal representation.
     * @return Ordinal string representation of the year number (e.g., "1st", "2nd", "3rd", "4th").
     */
    private static String getOrdinal(int n) {
        // Special case for 11th, 12th, and 13th
        if (n >= 11 && n <= 13) {
            return n + "th";
        }

        // Determine the ordinal suffix based on the last digit
        return switch (n % 10) {
            case 1 -> n + "st"; // Suffix for numbers ending in 1 (except 11)
            case 2 -> n + "nd"; // Suffix for numbers ending in 2 (except 12)
            case 3 -> n + "rd"; // Suffix for numbers ending in 3 (except 13)
            default -> n + "th"; // Default suffix for all other numbers
        };
    }
}
