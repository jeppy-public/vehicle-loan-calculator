package com.jptest.loan.processor;

import com.jptest.loan.constant.ErrorMessages;
import com.jptest.loan.service.LoanCalculatorService;
import com.jptest.loan.validator.LoanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * {@code ManualInputProcessor} class is responsible for processing loan input from manual console input.
 * <p>
 *     Extends {@link BaseProcessor} to inherit common loan calculation functionalities.
 *     This class is designed to interact with the user through the console to gather loan parameters,
 *     validate them, and then proceed to calculate and display the monthly installments.
 * </p>
 */
@Component
public class ManualInputProcessor extends BaseProcessor {
    /**
     * Minimum Down payment rate (percentage), injected from application properties.
     * <p>
     *     This rate is used as a threshold when validating the down payment amount provided by the user.
     * </p>
     */
    @Value("${loan.minimum.downpayment}")
    private BigDecimal minimumDownPaymentRate;

    private final LoanValidator loanValidator;

    /**
     * Constructor for {@code ManualInputProcessor}.
     * <p>
     *     Injects {@link LoanCalculatorService} for loan calculations and {@link LoanValidator}
     *     for input validation.
     * </p>
     *
     * @param loanCalculatorService Service for performing loan calculations.
     * @param loanValidator Validator for validating loan input parameters.
     */
    @Autowired
    public ManualInputProcessor(LoanCalculatorService loanCalculatorService, LoanValidator loanValidator) {
        super(loanCalculatorService);
        this.loanValidator = loanValidator;
    }

    /**
     * Processes loan input by prompting the user for loan details via the console.
     * <p>
     *     This method guides the user through entering vehicle and loan details step-by-step via the console.
     *     It validates each input to ensure correctness and provides feedback to the user.
     *     Upon successful input of all required parameters, it proceeds to calculate and display the monthly loan installment.
     * </p>
     */
    public void processInput() {
        // Use try-with-resources to ensure the Scanner is closed after use
        try (Scanner scanner = new Scanner(System.in)) {
            // Prompt for and validate vehicle type
            System.out.print("Enter vehicle type (car/motorcycle): ");
            String vehicleType = getVehicleType(scanner);
            if (vehicleType == null) return; // Exit if vehicle type is invalid

            // Prompt for and validate vehicle condition
            System.out.print("Enter vehicle condition (new/old): ");
            String vehicleCondition = getVehicleCondition(scanner);
            if (vehicleCondition == null) return; // Exit if vehicle condition is invalid

            // Prompt for and validate vehicle year
            System.out.print("Enter vehicle year (4 digits): ");
            int vehicleYear = getVehicleYear(scanner, vehicleCondition);
            if (vehicleYear == -1) return; // Exit if vehicle year is invalid

            // Prompt for and validate loan amount
            System.out.print("Enter total loan amount (up to 1 billion): ");
            double loanAmount = getLoanAmount(scanner);
            if (loanAmount == -1) return; // Exit if loan amount is invalid

            // Prompt for and validate loan tenor
            System.out.print("Enter loan tenor (1-6 years): ");
            int loanTenor = getLoanTenor(scanner);
            if (loanTenor == -1) return; // Exit if loan tenor is invalid

            // Prompt for and validate down payment amount
            System.out.print("Enter down payment amount: ");
            double downPayment = getDownPaymentAmount(scanner, new BigDecimal(loanAmount));
            if (downPayment == -1) return; // Exit if down payment is invalid

            // If all inputs are valid, calculate and print the monthly installment
            calculateAndPrintInstallment(vehicleType, vehicleCondition, vehicleYear, loanAmount, loanTenor, downPayment);

        } catch (NumberFormatException e) {
            // Handle exceptions for invalid number formats in user input
            System.out.println(ErrorMessages.INVALID_INPUT_FORMAT);
        } catch (IllegalArgumentException e) {
            // Handle general validation exceptions
            System.out.println(ErrorMessages.ERROR + e.getMessage());
        }
    }

    /**
     * Retrieves and validates vehicle type from user input via console.
     * <p>
     *     Uses {@link LoanValidator#isValidVehicleType(String)} to validate the input.
     * </p>
     *
     * @param scanner Scanner object to read user input from console.
     * @return Validated vehicle type string, or null if the input is invalid.
     */
    String getVehicleType(Scanner scanner) {
        String vehicleType = scanner.nextLine().trim();
        if (!loanValidator.isValidVehicleType(vehicleType)) {
            System.out.println(ErrorMessages.INVALID_VEHICLE_TYPE);
            return null;
        }
        return vehicleType;
    }

    /**
     * Retrieves and validates vehicle condition from user input via console.
     * <p>
     *     Uses {@link LoanValidator#isValidVehicleCondition(String)} to validate the input.
     * </p>
     *
     * @param scanner Scanner object to read user input from console.
     * @return Validated vehicle condition string, or null if the input is invalid.
     */
    String getVehicleCondition(Scanner scanner) {
        String vehicleCondition = scanner.nextLine().trim();
        if (!loanValidator.isValidVehicleCondition(vehicleCondition)) {
            System.out.println(ErrorMessages.INVALID_VEHICLE_CONDITION);
            return null;
        }
        return vehicleCondition;
    }

    /**
     * Retrieves and validates vehicle year from user input via console.
     * <p>
     *     Validates if the year is a four-digit number, not in the future, and consistent with
     *     the vehicle condition using methods in {@link LoanValidator}.
     * </p>
     *
     * @param scanner Scanner object to read user input from console.
     * @param vehicleCondition Vehicle condition string to provide context for year validation.
     * @return Validated vehicle year integer, or -1 if the input is invalid.
     */
    int getVehicleYear(Scanner scanner, String vehicleCondition) {
        int vehicleYear = Integer.parseInt(scanner.nextLine().trim());
        if (!loanValidator.isValidYearFourDigit(vehicleYear)) {
            System.out.println(ErrorMessages.INVALID_YEAR_4_DIGIT);
            return -1;
        }
        if (!loanValidator.isValidYearCompareWithCurrentYear(vehicleYear)) {
            System.out.println(ErrorMessages.INVALID_YEAR_COMPARE_CURRENT_YEAR);
            return -1;
        }
        if (!loanValidator.isValidVehicleIfNewCondition(vehicleCondition, vehicleYear)) {
            System.out.println(ErrorMessages.INVALID_VEHICLE_CONDITION_WITH_YEAR);
            return -1;
        }
        return vehicleYear;
    }

    /**
     * Retrieves and validates loan amount from user input via console.
     * <p>
     *     Uses {@link LoanValidator#isValidLoanAmount(double)} to validate the input.
     * </p>
     *
     * @param scanner Scanner object to read user input from console.
     * @return Validated loan amount double, or -1 if the input is invalid.
     */
    double getLoanAmount(Scanner scanner) {
        double loanAmount = Double.parseDouble(scanner.nextLine().trim());
        if (!loanValidator.isValidLoanAmount(loanAmount)) {
            System.out.println(ErrorMessages.INVALID_LOAN_AMOUNT);
            return -1;
        }
        return loanAmount;
    }

    /**
     * Retrieves and validates loan tenor from user input via console.
     * <p>
     *     Uses {@link LoanValidator#isValidLoanTenor(int)} to validate the input.
     * </p>
     *
     * @param scanner Scanner object to read user input from console.
     * @return Validated loan tenor integer, or -1 if the input is invalid.
     */
    int getLoanTenor(Scanner scanner) {
        int loanTenor = Integer.parseInt(scanner.nextLine().trim());
        if (!loanValidator.isValidLoanTenor(loanTenor)) {
            System.out.println(ErrorMessages.INVALID_TENOR);
            return -1;
        }
        return loanTenor;
    }

    /**
     * Retrieves and validates down payment amount from user input via console.
     * <p>
     *     Validates the down payment amount against the loan amount and minimum down payment rate
     *     using {@link LoanValidator#isValidDownPaymentAmount(double, BigDecimal, BigDecimal)}.
     * </p>
     *
     * @param scanner Scanner object to read user input from console.
     * @param loanAmount BigDecimal representing the loan amount for down payment validation context.
     * @return Validated down payment double, or -1 if the input is invalid.
     */
    double getDownPaymentAmount(Scanner scanner, BigDecimal loanAmount) {
        double downPayment = Double.parseDouble(scanner.nextLine().trim()); // Read and trim down payment input
        if (!loanValidator.isValidDownPaymentAmount(downPayment, loanAmount, minimumDownPaymentRate)) {
            System.out.println(ErrorMessages.INVALID_DOWN_PAYMENT_AMOUNT);
            return -1; // Return -1 if down payment is invalid
        }
        return downPayment; // Return the validated down payment
    }
}
