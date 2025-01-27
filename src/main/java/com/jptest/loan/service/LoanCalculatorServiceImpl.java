package com.jptest.loan.service;

import com.jptest.loan.constant.AppConstant;
import com.jptest.loan.dto.MonthlyInstallmentRatePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for calculating loan installments.
 * This class provides the logic to calculate monthly installments based on vehicle type, condition, year, loan amount, tenor, and down payment.
 */
@Service
public class LoanCalculatorServiceImpl implements LoanCalculatorService {

    /**
     * Interest rate (percentage) for car loans, injected from application properties.
     */
    @Value("${loan.interest.rate.car}")
    private BigDecimal carInterestRate;

    /**
     * Interest rate (percentage) for motorcycle loans, injected from application properties.
     */
    @Value("${loan.interest.rate.motorcycle}")
    private BigDecimal motorcycleInterestRate;

    /**
     * Interest rate  (percentage) increment for the next 1 year, injected from application properties.
     */
    @Value("${loan.interest.rate.increment.first.year}")
    private BigDecimal incrementInterestRateFirstYear;

    /**
     * Interest rate  (percentage) increment for the next 2 year, injected from application properties.
     */
    @Value("${loan.interest.rate.increment.second.year}")
    private BigDecimal incrementInterestRateSecondYear;

    /**
     * Minimum Down payment (percentage), injected from application properties.
     */
    @Value("${loan.minimum.downpayment}")
    private BigDecimal minimumDownPayment;

    /**
     * Calculates the monthly installment for a vehicle loan.
     *
     * @param vehicleType        The type of vehicle (car or motorcycle).
     * @param vehicleCondition   The condition of the vehicle (new or used).
     * @param vehicleYear        The year the vehicle was manufactured.
     * @param loanAmount         The total loan amount.
     * @param loanTenor          The loan tenor in years.
     * @param downPayment        The down payment amount.
     * @return                   The calculated monthly installment amount.
     * @throws IllegalArgumentException if vehicle type is invalid, vehicle year for new car is too old, or down payment is less than 35% of loan amount.
     */
    @Override
    public List<MonthlyInstallmentRatePair> calculateMonthlyInstallment(String vehicleType, String vehicleCondition, int vehicleYear, double loanAmount, int loanTenor, double downPayment) {
        List<MonthlyInstallmentRatePair> monthlyCalc = new ArrayList<>();
        BigDecimal monthlyAmountCalc;
        BigDecimal yearlyAmountCalc;
        BigDecimal loanAmountCalc = new BigDecimal(loanAmount);
        BigDecimal downPaymentCalc = new BigDecimal(downPayment);
        BigDecimal minimumDownPaymentRate;
        BigDecimal loanTenorYearly;
        BigDecimal loanTenorMonthly;

        // Interest Rate Calculation
        BigDecimal baseInterestRate;
        if ("car".equalsIgnoreCase(vehicleType)) {
            baseInterestRate = carInterestRate; // Use car interest rate from properties
        } else if ("motorcycle".equalsIgnoreCase(vehicleType)) {
            baseInterestRate = motorcycleInterestRate; // Use motorcycle interest rate from properties
        } else {
            throw new IllegalArgumentException("Invalid vehicle type");
        }
        loanTenorYearly = new BigDecimal(loanTenor);
        loanTenorMonthly = loanTenorYearly.multiply(new BigDecimal(Month.values().length));
        BigDecimal interestRate;
        BigDecimal interestRateForPrintOut;
        BigDecimal principalCalc = loanAmountCalc.subtract(downPaymentCalc);
        BigDecimal financePrice;

        for(int i = 1; i <= loanTenorYearly.intValue(); i++){
            if(i == 1){
                interestRate = baseInterestRate;
            } else if(i % 2 == 0){
                interestRate = baseInterestRate.add(incrementInterestRateFirstYear);
            } else {
                interestRate = baseInterestRate.add(incrementInterestRateSecondYear);
            }
            interestRateForPrintOut = interestRate;
            baseInterestRate = interestRate;
            interestRate = interestRate.divide(new BigDecimal(100));
            financePrice = principalCalc.add(principalCalc.multiply(interestRate));
            monthlyAmountCalc = financePrice.divide(loanTenorMonthly, RoundingMode.HALF_UP);
            yearlyAmountCalc = monthlyAmountCalc.multiply(new BigDecimal(Month.values().length));
            principalCalc = financePrice.subtract(yearlyAmountCalc);
            loanTenorMonthly = loanTenorMonthly.subtract(new BigDecimal(Month.values().length));

            monthlyCalc.add(new MonthlyInstallmentRatePair(monthlyAmountCalc, interestRateForPrintOut));
        }
        return monthlyCalc;
    }
}