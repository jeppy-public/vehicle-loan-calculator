package com.jptest.loan.dto;

import java.math.BigDecimal;

public record MonthlyInstallmentRatePair(BigDecimal amount, BigDecimal rate){
}
