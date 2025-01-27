package com.jptest.loan.processor;

import com.jptest.loan.constant.AppConstant;
import com.jptest.loan.constant.ErrorMessages;
import com.jptest.loan.service.LoanCalculatorService;
import com.jptest.loan.validator.LoanValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ManualInputProcessor} class.
 * This class verifies the input processing logic of the manual input processor,
 * focusing on validating user inputs for vehicle loan applications.
 * It includes tests for vehicle type, vehicle year, vehicle condition, loan amount,
 * and down payment amount, covering both valid and invalid input scenarios.
 */
@Tag("processor")
@ExtendWith(MockitoExtension.class)
public class ManualInputProcessorTest {

    private BigDecimal minimumDownPaymentRate;

    @Mock
    private LoanCalculatorService loanCalculatorService;

    private LoanValidator loanValidator;

    private ManualInputProcessor manualInputProcessor;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        manualInputProcessor = new ManualInputProcessor(loanCalculatorService, new LoanValidator());
        System.setOut(new PrintStream(outputStream));

        // Set minimumDownPaymentRate using reflection to simulate configuration
        ReflectionTestUtils.setField(manualInputProcessor, "minimumDownPaymentRate", new BigDecimal("25"));
    }

    @Test
    void testGetVehicleType_Valid() {
        outputStream.reset(); // Clear output stream to capture new output
        Scanner scanner = new Scanner("car\n"); // Simulate user input of 'car'

        String vehicleType = manualInputProcessor.getVehicleType(scanner);
        // Assert that no error message is printed (input is valid)
        assertTrue(outputStream.toString().isEmpty());
        // Assert that the method correctly returns the valid vehicle type 'car'
        assertEquals("car", vehicleType);
    }

    @Test
    void testGetVehicleType_Invalid() {
        Scanner scanner = new Scanner("plane\n"); // Simulate user input of 'plane' (invalid)

        String vehicleType = manualInputProcessor.getVehicleType(scanner);
        // Assert that an error message for invalid vehicle type is printed to the output stream
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_VEHICLE_TYPE));
        // Assert that the method returns null for invalid input
        assertNull(vehicleType);
    }

    @Test
    void testGetVehicleYearWithNewVehicle_Valid() {
        Scanner scanner = new Scanner("2024\n"); // Simulate user input of '2024' (valid year)

        double vehicleYear = manualInputProcessor.getVehicleYear(scanner, AppConstant.NEW_VEHICLE);
        // Assert that no error message is printed for valid year input
        assertTrue(outputStream.toString().isEmpty());
        // Assert that the method correctly parses and returns the year 2024 as double
        assertEquals(2024, vehicleYear);
    }

    @Test
    void testGetVehicleYearWithNewVehicle_NotYear_Invalid() {
        Scanner scanner = new Scanner("43\n"); // Simulate user input of '43' (invalid year format)

        double vehicleYear = manualInputProcessor.getVehicleYear(scanner, AppConstant.NEW_VEHICLE);
        // Assert that an error message for invalid year format (4 digits expected) is printed
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_YEAR_4_DIGIT));
        // Assert that the method returns -1 for invalid year format
        assertEquals(-1, vehicleYear);
    }

    @Test
    void testGetVehicleYearWithNewVehicle_Two_Years_Ago_Invalid() {
        Scanner scanner = new Scanner("2023\n"); // Simulate user input of '2023' (invalid for new vehicle)

        double vehicleYear = manualInputProcessor.getVehicleYear(scanner, AppConstant.NEW_VEHICLE);
        // Assert that an error message for invalid vehicle condition with year is printed
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_VEHICLE_CONDITION_WITH_YEAR));
        // Assert that the method returns -1 when year is invalid for the given vehicle condition
        assertEquals(-1, vehicleYear);
    }

    @Test
    void testGetVehicleYearWithNewVehicle_FutureYear_Invalid() {
        Scanner scanner = new Scanner("2026\n"); // Simulate user input of '2026' (future year - invalid)

        double vehicleYear = manualInputProcessor.getVehicleYear(scanner, AppConstant.NEW_VEHICLE);
        // Assert that error message for invalid future year is printed
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_YEAR_COMPARE_CURRENT_YEAR));
        // Assert that method returns -1 for future year input
        assertEquals(-1, vehicleYear);
    }

    @Test
    void testGetVehicleCondition_Valid() {
        Scanner scanner = new Scanner(AppConstant.NEW_VEHICLE+"\n"); // Simulate valid input 'new'

        String vehicleCondition = manualInputProcessor.getVehicleCondition(scanner);
        // Assert that no error message is printed for valid vehicle condition
        assertTrue(outputStream.toString().isEmpty());
        // Assert that the method correctly returns the valid vehicle condition 'new'
        assertEquals(AppConstant.NEW_VEHICLE, vehicleCondition);
    }

    @Test
    void testGetVehicleCondition_Invalid() {
        Scanner scanner = new Scanner("testCondition\n"); // Simulate invalid condition input

        String vehicleCondition = manualInputProcessor.getVehicleCondition(scanner);
        // Assert that error message for invalid vehicle condition is printed
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_VEHICLE_CONDITION));
        // Assert that the method returns null for invalid vehicle condition
        assertNull(vehicleCondition);
    }

    @Test
    void testGetLoanAmount_Valid() {
        Scanner scanner = new Scanner("100000000\n"); // Simulate valid loan amount input

        double loanAmount = manualInputProcessor.getLoanAmount(scanner);
        // Assert that loan amount is correctly read and parsed
        assertEquals(100000000, loanAmount);
        // Assert no error message for valid loan amount
        assertTrue(outputStream.toString().isEmpty());
    }

    @Test
    void testGetLoanAmount_Invalid() {
        Scanner scanner = new Scanner("1500000000\n"); // Simulate invalid loan amount (too large)

        double loanAmount = manualInputProcessor.getLoanAmount(scanner);
        // Assert that loan amount is -1, indicating invalid input
        assertEquals(-1, loanAmount);
        // Assert that error message for invalid loan amount (exceeding limit) is printed
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_LOAN_AMOUNT));
    }

    @Test
    void testGetDownPaymentAmount_Valid() {
        Scanner scanner = new Scanner("25000000\n"); // Valid down payment input

        double downPaymentAmount = manualInputProcessor.getDownPaymentAmount(scanner, new BigDecimal(100_000_000));
        // Assert that down payment amount is correctly calculated based on loan amount
        assertEquals(25_000_000, downPaymentAmount);
        // Assert no error message for valid down payment
        assertTrue(outputStream.toString().isEmpty());
    }

    @Test
    void testGetDownPaymentAmount_Invalid() {
        Scanner scanner = new Scanner("24999999\n"); // Invalid down payment (below minimum rate)

        double downPaymentAmount = manualInputProcessor.getDownPaymentAmount(scanner, new BigDecimal(100_000_000));
        // Assert that down payment amount is -1, indicating invalid input
        assertEquals(-1, downPaymentAmount);
        // Assert error message for invalid down payment (below minimum required) is printed
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_DOWN_PAYMENT_AMOUNT));
    }
}
