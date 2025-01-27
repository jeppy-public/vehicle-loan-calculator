package com.jptest.loan.processor;

import com.jptest.loan.constant.ErrorMessages;
import com.jptest.loan.service.LoanCalculatorService;
import com.jptest.loan.validator.LoanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.jptest.loan.constant.ErrorMessages;
import com.jptest.loan.service.LoanCalculatorService;
import com.jptest.loan.validator.LoanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * {@code FileInputProcessor} class is responsible for processing loan input from a file.
 * <p>
 *     It extends {@link BaseProcessor} to inherit common loan calculation functionalities.
 *     The class is designed to read loan parameters from a specified file, validate these parameters,
 *     and then calculate and print the monthly installments.
 * </p>
 */
@Component
public class FileInputProcessor extends BaseProcessor {

    private final LoanValidator loanValidator;

    /**
     * Minimum Down payment rate (percentage), injected from application properties.
     * <p>
     *     This rate is used as a threshold when validating the down payment amount to ensure
     *     it meets the minimum requirement based on the loan amount.
     * </p>
     */
    @Value("${loan.minimum.downpayment}")
    private BigDecimal minimumDownPaymentRate;

    /**
     * Constructor for {@code FileInputProcessor}.
     * <p>
     *     Injects {@link LoanCalculatorService} for loan calculations and {@link LoanValidator}
     *     for validating input parameters read from the file.
     * </p>
     *
     * @param loanCalculatorService Service for performing loan calculations.
     * @param loanValidator Validator for validating loan input parameters.
     */
    @Autowired
    public FileInputProcessor(LoanCalculatorService loanCalculatorService, LoanValidator loanValidator) {
        super(loanCalculatorService);
        this.loanValidator = loanValidator;
    }

    /**
     * Processes loan input from a file specified by the file path.
     * <p>
     *     This method reads loan parameters from each line of the file, in a predefined order:
     *     vehicle type, vehicle condition, vehicle year, loan amount, loan tenor, and down payment.
     *     It validates each parameter and, upon successful validation, proceeds to calculate
     *     and print the monthly installments.
     * </p>
     * <p>
     *     The method handles file reading errors, number format exceptions, and validation exceptions,
     *     providing informative error messages to the console in case of any issues.
     * </p>
     *
     * @param filePath The path to the input file. The file should contain exactly 6 lines,
     *                 each representing a loan parameter in the order specified above.
     */
    public void processFile(String filePath) {
        try {
            List<String> lines;
            // Use try-with-resources to ensure that the stream is closed after usage
            try (var streamLines = Files.lines(Paths.get(filePath))) {
                lines = streamLines.map(String::trim) // Trim each line to remove leading/trailing whitespace
                        .filter(line -> !line.isEmpty()) // Filter out any empty lines
                        .toList(); // Collect the lines into a List
            }

            // Validate if the file contains exactly 6 lines as expected
            if (lines.size() != 6) {
                System.out.println(ErrorMessages.INVALID_FILE_FORMAT_SIX_LINES);
                return; // Exit if the file does not contain the expected number of lines
            }

            // Extract and parse loan parameters from the lines read from the file
            String vehicleType = lines.get(0); // Vehicle type is on the first line
            String vehicleCondition = lines.get(1); // Vehicle condition is on the second line
            int vehicleYear = Integer.parseInt(lines.get(2)); // Vehicle year is on the third line
            double loanAmount = Double.parseDouble(lines.get(3)); // Loan amount is on the fourth line
            int loanTenor = Integer.parseInt(lines.get(4)); // Loan tenor is on the fifth line
            double downPayment = Double.parseDouble(lines.get(5)); // Down payment is on the sixth line

            // Validate each extracted parameter
            vehicleType = getVehicleType(vehicleType);
            if (vehicleType == null) return; // Stop processing if vehicle type is invalid

            vehicleCondition = getVehicleCondition(vehicleCondition);
            if (vehicleCondition == null) return; // Stop processing if vehicle condition is invalid

            vehicleYear = getVehicleYear(vehicleYear, vehicleCondition);
            if (vehicleYear == -1) return; // Stop processing if vehicle year is invalid

            loanAmount = getLoanAmount(loanAmount);
            if (loanAmount == -1) return; // Stop processing if loan amount is invalid

            loanTenor = getLoanTenor(loanTenor);
            if (loanTenor == -1) return; // Stop processing if loan tenor is invalid

            downPayment = getDownPaymentAmount(downPayment, new BigDecimal(loanAmount));
            if (downPayment == -1) return; // Stop processing if down payment is invalid

            // If all parameters are valid, calculate and print the monthly installments
            calculateAndPrintInstallment(vehicleType, vehicleCondition, vehicleYear, loanAmount, loanTenor, downPayment);

        } catch (IOException e) {
            // Handle file not found or file reading issues
            System.out.println(ErrorMessages.COULD_NOT_READ_FILE + filePath);
        } catch (NumberFormatException e) {
            // Handle exceptions when parsing numbers from the file lines
            System.out.println(ErrorMessages.INVALID_INPUT_FORMAT);
        } catch (IllegalArgumentException e) {
            // Handle validation exceptions or any business logic exceptions
            System.out.println(ErrorMessages.ERROR + e.getMessage());
        }
    }

    /**
     * Validates and retrieves the vehicle type from the input string.
     * <p>
     *     Utilizes {@link LoanValidator#isValidVehicleType(String)} to check if the provided
     *     vehicle type is valid against predefined criteria.
     * </p>
     *
     * @param vehicleType The vehicle type string to validate.
     * @return The validated vehicle type string, or null if the input is not a valid vehicle type.
     */
    String getVehicleType(String vehicleType) {
        if (!loanValidator.isValidVehicleType(vehicleType)) {
            System.out.println(ErrorMessages.INVALID_VEHICLE_TYPE);
            return null; // Return null if vehicle type is invalid
        }
        return vehicleType; // Return the validated vehicle type
    }

    /**
     * Validates and retrieves the vehicle condition from the input string.
     * <p>
     *     Utilizes {@link LoanValidator#isValidVehicleCondition(String)} to check if the provided
     *     vehicle condition is valid (e.g., "new" or "old").
     * </p>
     *
     * @param vehicleCondition The vehicle condition string to validate.
     * @return The validated vehicle condition string, or null if the input is not a valid vehicle condition.
     */
    String getVehicleCondition(String vehicleCondition) {
        if (!loanValidator.isValidVehicleCondition(vehicleCondition)) {
            System.out.println(ErrorMessages.INVALID_VEHICLE_CONDITION);
            return null; // Return null if vehicle condition is invalid
        }
        return vehicleCondition; // Return the validated vehicle condition
    }

    /**
     * Validates and retrieves the vehicle year from the input string and vehicle condition.
     * <p>
     *     Performs several validations on the vehicle year:
     *     1. Checks if it is a four-digit number using {@link LoanValidator#isValidYearFourDigit(int)}.
     *     2. Verifies that it is not a future year using {@link LoanValidator#isValidYearCompareWithCurrentYear(int)}.
     *     3. Ensures that it is consistent with the vehicle condition (e.g., a 'new' vehicle should not have an old year)
     *        using {@link LoanValidator#isValidVehicleIfNewCondition(String, int)}.
     * </p>
     *
     * @param vehicleYear Integer representing the vehicle year to validate.
     * @param vehicleCondition String representing the vehicle condition, used for context validation.
     * @return The validated vehicle year integer, or -1 if the input is not a valid vehicle year.
     */
    int getVehicleYear(int vehicleYear, String vehicleCondition) {
        if (!loanValidator.isValidYearFourDigit(vehicleYear)) {
            System.out.println(ErrorMessages.INVALID_YEAR_4_DIGIT);
            return -1; // Return -1 if vehicle year is not a four-digit number
        }
        if (!loanValidator.isValidYearCompareWithCurrentYear(vehicleYear)) {
            System.out.println(ErrorMessages.INVALID_YEAR_COMPARE_CURRENT_YEAR);
            return -1; // Return -1 if vehicle year is in the future
        }
        if (!loanValidator.isValidVehicleIfNewCondition(vehicleCondition, vehicleYear)) {
            System.out.println(ErrorMessages.INVALID_VEHICLE_CONDITION_WITH_YEAR);
            return -1; // Return -1 if vehicle year is invalid for 'new' condition
        }
        return vehicleYear; // Return the validated vehicle year
    }

    /**
     * Validates and retrieves the loan amount from the input double.
     * <p>
     *     Uses {@link LoanValidator#isValidLoanAmount(double)} to ensure that the loan amount
     *     is within acceptable limits and is a valid amount.
     * </p>
     *
     * @param loanAmount Double representing the loan amount to validate.
     * @return The validated loan amount double, or -1 if the input is not a valid loan amount.
     */
    double getLoanAmount(double loanAmount) {
        if (!loanValidator.isValidLoanAmount(loanAmount)) {
            System.out.println(ErrorMessages.INVALID_LOAN_AMOUNT);
            return -1; // Return -1 if loan amount is invalid
        }
        return loanAmount; // Return the validated loan amount
    }

    /**
     * Validates and retrieves the loan tenor from the input integer.
     * <p>
     *     Validates the loan tenor to ensure it is within the allowed range (e.g., 1-6 years)
     *     using {@link LoanValidator#isValidLoanTenor(int)}.
     * </p>
     *
     * @param loanTenor Integer representing the loan tenor to validate.
     * @return The validated loan tenor integer, or -1 if the input is not a valid loan tenor.
     */
    int getLoanTenor(int loanTenor) {
        if (!loanValidator.isValidLoanTenor(loanTenor)) {
            System.out.println(ErrorMessages.INVALID_TENOR);
            return -1; // Return -1 if loan tenor is invalid
        }
        return loanTenor; // Return the validated loan tenor
    }

    /**
     * Validates and retrieves the down payment amount from the input double.
     * <p>
     *     Validates the down payment amount to ensure it is sufficient based on the loan amount
     *     and the minimum down payment rate, using
     *     {@link LoanValidator#isValidDownPaymentAmount(double, BigDecimal, BigDecimal)}.
     * </p>
     *
     * @param downPayment Double representing the down payment amount to validate.
     * @param loanAmount BigDecimal representing the total loan amount, used for down payment validation context.
     * @return The validated down payment double, or -1 if the input is not a valid down payment amount.
     */
    double getDownPaymentAmount(double downPayment, BigDecimal loanAmount) {
        if (!loanValidator.isValidDownPaymentAmount(downPayment, loanAmount, minimumDownPaymentRate)) {
            System.out.println(ErrorMessages.INVALID_DOWN_PAYMENT_AMOUNT);
            return -1; // Return -1 if down payment is invalid
        }
        return downPayment; // Return the validated down payment
    }
}
