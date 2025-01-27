package com.jptest.loan.config;

import com.jptest.loan.service.LoanCalculatorService;
import com.jptest.loan.service.LoanCalculatorServiceImpl;
import com.jptest.loan.validator.LoanValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@code TestConfig} is a configuration class specifically for unit tests.
 * It sets up the necessary beans and configurations required for
 * testing components like services and validators in isolation.
 *
 * This configuration is used in test contexts to provide mocked or
 * simplified implementations of dependencies, allowing for focused
 * unit testing without the overhead of a full application context.
 */
@Configuration
public class TestConfig {
    @Bean
    public LoanCalculatorService loanCalculatorService() {
        return new LoanCalculatorServiceImpl();
    }

    @Bean
    public LoanValidator loanValidator() {
        return new LoanValidator();
    }
}
