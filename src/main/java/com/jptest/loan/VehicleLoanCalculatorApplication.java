package com.jptest.loan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class VehicleLoanCalculatorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(VehicleLoanCalculatorApplication.class, args);
        context.close();
    }

}
