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
import java.util.List;
import java.util.Scanner;

@Component
public class FileInputProcessor extends BaseProcessor {

    private final LoanValidator loanValidator;

    /**
     * Minimum Down payment (percentage), injected from application properties.
     */
    @Value("${loan.minimum.downpayment}")
    private BigDecimal minimumDownPaymentRate;

    @Autowired
    public FileInputProcessor(LoanCalculatorService loanCalculatorService, LoanValidator loanValidator) {
        super(loanCalculatorService);
        this.loanValidator = loanValidator;
    }

    public void processFile(String filePath) {
        try {
            List<String> lines;
            try (var streamLines = Files.lines(Paths.get(filePath))) {
                lines = streamLines.map(String::trim)
                        .filter(line -> !line.isEmpty())
                        .toList();
            }

            if (lines.size() != 6) {
                System.out.println(ErrorMessages.INVALID_FILE_FORMAT_SIX_LINES);
                return;
            }

            String vehicleType = lines.get(0);
            String vehicleCondition = lines.get(1);
            int vehicleYear = Integer.parseInt(lines.get(2));
            double loanAmount = Double.parseDouble(lines.get(3));
            int loanTenor = Integer.parseInt(lines.get(4));
            double downPayment = Double.parseDouble(lines.get(5));

            // Validations
            vehicleType = getVehicleType(vehicleType);
            if (vehicleType == null) return;

            vehicleCondition = getVehicleCondition(vehicleCondition);
            if (vehicleCondition == null) return;

            vehicleYear = getVehicleYear(vehicleYear, vehicleCondition);
            if (vehicleYear == -1) return;

            loanAmount = getLoanAmount(loanAmount);
            if (loanAmount == -1) return;

            loanTenor = getLoanTenor(loanTenor);
            if (loanTenor == -1) return;

            downPayment = getDownPaymentAmount(downPayment, new BigDecimal(loanAmount));
            if (downPayment == -1) return;

            // Down payment validation is in service layer
            calculateAndPrintInstallment(vehicleType, vehicleCondition, vehicleYear, loanAmount, loanTenor, downPayment);

        } catch (IOException e) {
            System.out.println(ErrorMessages.COULD_NOT_READ_FILE + filePath);
        } catch (NumberFormatException e) {
            System.out.println(ErrorMessages.INVALID_INPUT_FORMAT);
        } catch (IllegalArgumentException e) {
            System.out.println(ErrorMessages.ERROR + e.getMessage());
        }
    }

    String getVehicleType(String vehicleType) {
        if (!loanValidator.isValidVehicleType(vehicleType)) {
            System.out.println(ErrorMessages.INVALID_VEHICLE_TYPE);
            return null;
        }
        return vehicleType;
    }

    String getVehicleCondition(String vehicleCondition) {
        if (!loanValidator.isValidVehicleCondition(vehicleCondition)) {
            System.out.println(ErrorMessages.INVALID_VEHICLE_CONDITION);
            return null;
        }
        return vehicleCondition;
    }

    int getVehicleYear(int vehicleYear, String vehicleCondition) {
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

    double getLoanAmount(double loanAmount) {
        if (!loanValidator.isValidLoanAmount(loanAmount)) {
            System.out.println(ErrorMessages.INVALID_LOAN_AMOUNT);
            return -1;
        }
        return loanAmount;
    }

    int getLoanTenor(int loanTenor) {
        if (!loanValidator.isValidLoanTenor(loanTenor)) {
            System.out.println(ErrorMessages.INVALID_TENOR);
            return -1;
        }
        return loanTenor;
    }

    double getDownPaymentAmount(double downPayment, BigDecimal loanAmount) {
        if (!loanValidator.isValidDownPaymentAmount(downPayment, loanAmount, minimumDownPaymentRate)) {
            System.out.printf(ErrorMessages.INVALID_DOWN_PAYMENT_AMOUNT, minimumDownPaymentRate);
            return -1;
        }
        return downPayment;

        //double downPayment = Double.parseDouble(scanner.nextLine().trim());
    }
}
