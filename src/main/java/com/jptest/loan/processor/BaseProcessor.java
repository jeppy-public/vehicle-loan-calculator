package com.jptest.loan.processor;

import com.jptest.loan.dto.MonthlyInstallmentRatePair;
import com.jptest.loan.service.LoanCalculatorService;

import java.util.List;

public class BaseProcessor {

    private final LoanCalculatorService loanCalculatorService;

    public BaseProcessor(LoanCalculatorService loanCalculatorService) {
        this.loanCalculatorService = loanCalculatorService;
    }

    public void calculateAndPrintInstallment(String vehicleType, String vehicleCondition, int vehicleYear, double loanAmount, int loanTenor, double downPayment) {
        try {
            List<MonthlyInstallmentRatePair> monthlyInstallment = loanCalculatorService.calculateMonthlyInstallment(
                    vehicleType, vehicleCondition, vehicleYear, loanAmount, loanTenor, downPayment
            );
            int year = 1;
            for(MonthlyInstallmentRatePair monthlyPair: monthlyInstallment){
                System.out.printf("\n%s year with Monthly installment: Rp %,.2f, Interest rate: %.1f%%%n", getOrdinal(year++), monthlyPair.amount(), monthlyPair.rate());
            }
            System.out.print("\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String getOrdinal(int n) {
        if (n >= 11 && n <= 13) {
            return n + "th";
        }

        return switch (n % 10) {
            case 1 -> n + "st";
            case 2 -> n + "nd";
            case 3 -> n + "rd";
            default -> n + "th";
        };
    }
}
