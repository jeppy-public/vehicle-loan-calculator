package com.jptest.loan.config;

import com.jptest.loan.service.LoanCalculatorService;
import com.jptest.loan.service.LoanCalculatorServiceImpl;
import com.jptest.loan.validator.LoanValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    public LoanCalculatorService loanCalculatorService() {
        return new LoanCalculatorServiceImpl();
    }
}
