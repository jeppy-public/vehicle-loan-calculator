package com.jptest.loan.service;

import com.jptest.loan.config.TestConfig;
import com.jptest.loan.dto.MonthlyInstallmentRatePair;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("service")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource(properties = {
        "loan.interest.rate.car=8",
        "loan.interest.rate.motorcycle=9",
        "loan.interest.rate.increment.first.year=0.1",
        "loan.interest.rate.increment.second.year=0.5",
        "loan.minimum.downpayment=25"
})
/**
 * Unit tests for {@link LoanCalculatorServiceImpl}.
 * This class contains test cases to verify the loan calculation functionalities
 * provided by the LoanCalculatorServiceImpl. It specifically tests the calculation
 * of monthly installments under various loan conditions and configurations.
 */
@SpringBootTest
class LoanCalculatorServiceImplTest {
    @Autowired
    private LoanCalculatorService loanCalculatorService;

    @Test
    void calculateMonthlyInstallment_ValidInput_ManualProcessor() {
        String vehicleType = "car";
        String vehicleCondition = "new";
        int vehicleYear = 2024;
        double loanAmount = 100_000_000;
        int loanTenor = 3; // Loan term in years
        double downPayment = 25_000_000;

        // Invoke the method to calculate monthly installments
        List<MonthlyInstallmentRatePair> monthlyInstallment = loanCalculatorService.calculateMonthlyInstallment(vehicleType, vehicleCondition, vehicleYear, loanAmount, loanTenor, downPayment);

        // Define the expected monthly installments for each year of the loan term
        List<MonthlyInstallmentRatePair> expectedList = List.of(
                new MonthlyInstallmentRatePair(new BigDecimal("2250000.00"), new BigDecimal(8)),
                new MonthlyInstallmentRatePair(new BigDecimal("2432250.00"), new BigDecimal("8.1")),
                new MonthlyInstallmentRatePair(new BigDecimal("2641423.50"), new BigDecimal("8.6")));

        // Assert that the size of the calculated installments list matches the expected list size
        assertEquals(expectedList.size(), monthlyInstallment.size());
        // Iterate through each expected installment and compare with the calculated installment
        for (int i = 0; i < expectedList.size(); i++) {
            // Assert that the calculated installment amount matches the expected amount, scaled and rounded
            assertEquals(
                    expectedList.get(i).amount().setScale(2, RoundingMode.HALF_UP),
                    monthlyInstallment.get(i).amount().setScale(2, RoundingMode.HALF_UP)
            );
            // Assert that the calculated interest rate matches the expected rate, scaled and rounded
            assertEquals(
                    expectedList.get(i).rate().setScale(1, RoundingMode.HALF_UP),
                    monthlyInstallment.get(i).rate().setScale(1, RoundingMode.HALF_UP)
            );
        }
    }
}
