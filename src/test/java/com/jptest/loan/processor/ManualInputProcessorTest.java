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

        // Manually set the minimumDownPaymentRate
        ReflectionTestUtils.setField(manualInputProcessor, "minimumDownPaymentRate", new BigDecimal("25"));
    }

    @Test
    void testGetVehicleType_Valid() {
        outputStream.reset();
        Scanner scanner = new Scanner("car\n");

        String vehicleType = manualInputProcessor.getVehicleType(scanner);
        assertTrue(outputStream.toString().isEmpty());
        assertEquals("car", vehicleType);
    }

    @Test
    void testGetVehicleType_Invalid() {
        Scanner scanner = new Scanner("plane\n");

        String vehicleType = manualInputProcessor.getVehicleType(scanner);
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_VEHICLE_TYPE));
        assertNull(vehicleType);
    }

    @Test
    void testGetVehicleYearWithNewVehicle_Valid() {
        Scanner scanner = new Scanner("2024\n");

        double vehicleYear = manualInputProcessor.getVehicleYear(scanner, AppConstant.NEW_VEHICLE);
        assertTrue(outputStream.toString().isEmpty());
        assertEquals(2024, vehicleYear);
    }

    @Test
    void testGetVehicleYearWithNewVehicle_NotYear_Invalid() {
        Scanner scanner = new Scanner("43\n");

        double vehicleYear = manualInputProcessor.getVehicleYear(scanner, AppConstant.NEW_VEHICLE);
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_YEAR_4_DIGIT));
        assertEquals(-1, vehicleYear);
    }

    @Test
    void testGetVehicleYearWithNewVehicle_Two_Years_Ago_Invalid() {
        Scanner scanner = new Scanner("2023\n");

        double vehicleYear = manualInputProcessor.getVehicleYear(scanner, AppConstant.NEW_VEHICLE);
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_VEHICLE_CONDITION_WITH_YEAR));
        assertEquals(-1, vehicleYear);
    }

    @Test
    void testGetVehicleYearWithNewVehicle_FutureYear_Invalid() {
        Scanner scanner = new Scanner("2026\n");

        double vehicleYear = manualInputProcessor.getVehicleYear(scanner, AppConstant.NEW_VEHICLE);
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_YEAR_COMPARE_CURRENT_YEAR));
        assertEquals(-1, vehicleYear);
    }

    @Test
    void testGetVehicleCondition_Valid() {
        Scanner scanner = new Scanner(AppConstant.NEW_VEHICLE+"\n");

        String vehicleCondition = manualInputProcessor.getVehicleCondition(scanner);
        assertTrue(outputStream.toString().isEmpty());
        assertEquals(AppConstant.NEW_VEHICLE, vehicleCondition);
    }

    @Test
    void testGetVehicleCondition_Invalid() {
        Scanner scanner = new Scanner("testCondition\n");

        String vehicleCondition = manualInputProcessor.getVehicleCondition(scanner);
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_VEHICLE_CONDITION));
        assertNull(vehicleCondition);

    }

    @Test
    void testGetLoanAmount_Valid() {
        Scanner scanner = new Scanner("100000000\n");

        double loanAmount = manualInputProcessor.getLoanAmount(scanner);
        assertEquals(100000000, loanAmount);
        assertTrue(outputStream.toString().isEmpty());
    }

    @Test
    void testGetLoanAmount_Invalid() {
        Scanner scanner = new Scanner("1500000000\n");

        double loanAmount = manualInputProcessor.getLoanAmount(scanner);
        assertEquals(-1, loanAmount);
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_LOAN_AMOUNT));
    }
    @Test
    void testGetDownPaymentAmount_Valid() {
        Scanner scanner = new Scanner("25000000\n");

        double downPaymentAmount = manualInputProcessor.getDownPaymentAmount(scanner, new BigDecimal(100_000_000));
        assertEquals(25_000_000, downPaymentAmount);
        assertTrue(outputStream.toString().isEmpty());
    }
    @Test
    void testGetDownPaymentAmount_Invalid() {
        Scanner scanner = new Scanner("24999999\n");

        double downPaymentAmount = manualInputProcessor.getDownPaymentAmount(scanner, new BigDecimal(100_000_000));
        assertEquals(-1, downPaymentAmount);
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_DOWN_PAYMENT_AMOUNT));
    }
}
