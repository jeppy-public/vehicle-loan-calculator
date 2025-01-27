package com.jptest.loan.dto;

import java.math.BigDecimal;

/**
 * {@code MonthlyInstallmentRatePair} record to hold monthly installment and rate.
 * <p>This record encapsulates a pair of values: the monthly installment amount and the corresponding interest rate.
 * It is used to return the calculated monthly installment and the interest rate applied in the loan calculation.</p>
 *
 * @param amount Monthly installment amount. Represents the calculated monthly payment amount.
 * @param rate Interest rate. Represents the interest rate used for the loan calculation.
 */
public record MonthlyInstallmentRatePair(BigDecimal amount, BigDecimal rate){
}
