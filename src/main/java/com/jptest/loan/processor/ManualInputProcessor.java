package com.jptest.loan.processor;

import com.jptest.loan.constant.ErrorMessages;
import com.jptest.loan.service.LoanCalculatorService;
import com.jptest.loan.validator.LoanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class ManualInputProcessor extends BaseProcessor{
    /**
     * Minimum Down payment (percentage), injected from application properties.
     */
    @Value("${loan.minimum.downpayment}")
    private BigDecimal minimumDownPaymentRate;

    private final LoanValidator loanValidator;

    @Autowired
    public ManualInputProcessor(LoanCalculatorService loanCalculatorService, LoanValidator loanValidator) {
        super(loanCalculatorService);
        this.loanValidator = loanValidator;
    }

    public void processInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter vehicle type (car/motorcycle): ");
            String vehicleType = getVehicleType(scanner);
            if (vehicleType == null) return;

            System.out.print("Enter vehicle condition (new/old): ");
            String vehicleCondition = getVehicleCondition(scanner);
            if (vehicleCondition == null) return;

            System.out.print("Enter vehicle year (4 digits): ");
            int vehicleYear = getVehicleYear(scanner, vehicleCondition);
            if (vehicleYear == -1) return;

            System.out.print("Enter total loan amount (up to 1 billion): ");
            double loanAmount = getLoanAmount(scanner);
            if (loanAmount == -1) return;

            System.out.print("Enter loan tenor (1-6 years): ");
            int loanTenor = getLoanTenor(scanner);
            if (loanTenor == -1) return;

            System.out.print("Enter down payment amount: ");
            double downPayment = getDownPaymentAmount(scanner, new BigDecimal(loanAmount));
            if (downPayment == -1) return;

            // Down payment validation is in service layer
            calculateAndPrintInstallment(vehicleType, vehicleCondition, vehicleYear, loanAmount, loanTenor, downPayment);

        } catch (NumberFormatException e) {
            System.out.println(ErrorMessages.INVALID_INPUT_FORMAT);
        } catch (IllegalArgumentException e) {
            System.out.println(ErrorMessages.ERROR + e.getMessage());
        }
    }

    String getVehicleType(Scanner scanner) {
        String vehicleType = scanner.nextLine().trim();
        if (!loanValidator.isValidVehicleType(vehicleType)) {
            System.out.println(ErrorMessages.INVALID_VEHICLE_TYPE);
            return null;
        }
        return vehicleType;
    }

    String getVehicleCondition(Scanner scanner) {
        String vehicleCondition = scanner.nextLine().trim();
        if (!loanValidator.isValidVehicleCondition(vehicleCondition)) {
            System.out.println(ErrorMessages.INVALID_VEHICLE_CONDITION);
            return null;
        }
        return vehicleCondition;
    }

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

    double getLoanAmount(Scanner scanner) {
        double loanAmount = Double.parseDouble(scanner.nextLine().trim());
        if (!loanValidator.isValidLoanAmount(loanAmount)) {
            System.out.println(ErrorMessages.INVALID_LOAN_AMOUNT);
            return -1;
        }
        return loanAmount;
    }

    int getLoanTenor(Scanner scanner) {
        int loanTenor = Integer.parseInt(scanner.nextLine().trim());
        if (!loanValidator.isValidLoanTenor(loanTenor)) {
            System.out.println(ErrorMessages.INVALID_TENOR);
            return -1;
        }
        return loanTenor;
    }

    double getDownPaymentAmount(Scanner scanner, BigDecimal loanAmount) {
        double downPayment = Double.parseDouble(scanner.nextLine().trim());
        if (!loanValidator.isValidDownPaymentAmount(downPayment, loanAmount, minimumDownPaymentRate)) {
            System.out.println(ErrorMessages.INVALID_DOWN_PAYMENT_AMOUNT);
            return -1;
        }
        return downPayment;
    }
}
