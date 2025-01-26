package com.jptest.loan.service;

import com.jptest.loan.dto.MonthlyInstallmentRatePair;

import java.util.List;

public interface LoanCalculatorService {
    List<MonthlyInstallmentRatePair> calculateMonthlyInstallment(String vehicleType, String vehicleCondition, int vehicleYear, double loanAmount, int loanTenor, double downPayment);
}
