package com.jptest.loan.processor;

import com.jptest.loan.constant.ErrorMessages;
import com.jptest.loan.service.LoanCalculatorService;
import com.jptest.loan.validator.LoanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@Component
public class FileInputProcessor extends BaseProcessor {

    private final LoanValidator loanValidator;

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
            if (loanValidator.isValidVehicleType(vehicleType)) {
                System.out.println(ErrorMessages.INVALID_VEHICLE_TYPE);
                return;
            }
            if (loanValidator.isValidVehicleCondition(vehicleCondition)) {
                System.out.println(ErrorMessages.INVALID_VEHICLE_CONDITION);
                return;
            }
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
            if (loanValidator.isValidLoanAmount(loanAmount)) {
                System.out.println(ErrorMessages.INVALID_AMOUNT);
                return;
            }
            if (loanValidator.isValidLoanTenor(loanTenor)) {
                System.out.println(ErrorMessages.INVALID_TENOR);
                return;
            }

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
}
