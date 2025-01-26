package com.jptest.loan.processor;

import com.jptest.loan.constant.ErrorMessages;
import com.jptest.loan.service.LoanCalculatorService;
import com.jptest.loan.validator.LoanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ManualInputProcessor extends BaseProcessor{

    private final LoanValidator loanValidator;

    @Autowired
    public ManualInputProcessor(LoanCalculatorService loanCalculatorService, LoanValidator loanValidator) {
        super(loanCalculatorService);
        this.loanValidator = loanValidator;
    }

    public void processInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter vehicle type (car/motorcycle): ");
            String vehicleType = scanner.nextLine().trim();
            if (loanValidator.isValidVehicleType(vehicleType)) {
                System.out.println(ErrorMessages.INVALID_VEHICLE_TYPE);
                return;
            }

            System.out.print("Enter vehicle condition (new/old): ");
            String vehicleCondition = scanner.nextLine().trim();
            if (loanValidator.isValidVehicleCondition(vehicleCondition)) {
                System.out.println(ErrorMessages.INVALID_VEHICLE_CONDITION);
                return;
            }

            System.out.print("Enter vehicle year (4 digits): ");
            int vehicleYear = Integer.parseInt(scanner.nextLine().trim());
            if (loanValidator.isValidYearFourDigit(vehicleYear)) {
                System.out.println(ErrorMessages.INVALID_YEAR_4_DIGIT);
                return;
            }
            if (loanValidator.isValidYearCompareWithCurrentYear(vehicleYear)) {
                System.out.println(ErrorMessages.INVALID_YEAR_COMPARE_CURRENT_YEAR);
                return;
            }

            if (loanValidator.isValidVehicleIfNewCondition(vehicleCondition, vehicleYear)) {
                System.out.println(ErrorMessages.INVALID_VEHICLE_CONDITION_WITH_YEAR);
                return;
            }

            System.out.print("Enter total loan amount (up to 1 billion): ");
            double loanAmount = Double.parseDouble(scanner.nextLine().trim());
            if (loanValidator.isValidLoanAmount(loanAmount)) {
                System.out.println(ErrorMessages.INVALID_AMOUNT);
                return;
            }

            System.out.print("Enter loan tenor (1-6 years): ");
            int loanTenor = Integer.parseInt(scanner.nextLine().trim());
            if (loanValidator.isValidLoanTenor(loanTenor)) {
                System.out.println(ErrorMessages.INVALID_TENOR);
                return;
            }

            System.out.print("Enter down payment amount: ");
            double downPayment = Double.parseDouble(scanner.nextLine().trim());

            // Down payment validation is in service layer
            calculateAndPrintInstallment(vehicleType, vehicleCondition, vehicleYear, loanAmount, loanTenor, downPayment);

        } catch (NumberFormatException e) {
            System.out.println(ErrorMessages.INVALID_INPUT_FORMAT);
        } catch (IllegalArgumentException e) {
            System.out.println(ErrorMessages.ERROR + e.getMessage());
        }
    }
}
