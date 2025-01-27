package com.jptest.loan.processor;

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
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link FileInputProcessor} class.
 * This class contains tests to verify the functionality of processing
 * loan applications from input files. It includes tests for valid file processing,
 * file not found scenarios, invalid file formats, and invalid data inputs.
 */
@Tag("processor")
@ExtendWith(MockitoExtension.class)
public class FileInputProcessorTest {

    @Mock
    private LoanCalculatorService loanCalculatorService;

    private LoanValidator loanValidator;

    private FileInputProcessor fileInputProcessor;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private final String validFilePath = "input.txt"; // Path to a valid input file for testing

    @BeforeEach
    void setUp() throws IOException {
        fileInputProcessor = new FileInputProcessor(loanCalculatorService, new LoanValidator());
        System.setOut(new PrintStream(outputStream));

        // Set minimumDownPaymentRate using reflection to bypass constructor injection
        ReflectionTestUtils.setField(fileInputProcessor, "minimumDownPaymentRate", new BigDecimal("25"));

        // Create a valid input file for testing with loan details
        String content = "car\nnew\n2024\n100000\n5\n35000";
        Files.write(Paths.get(validFilePath), content.getBytes());
    }

    @Test
    void testProcessInput_ValidFile() {
        // Execute the file processing with a valid file path
        fileInputProcessor.processFile(validFilePath);

        // Verify that the loanCalculatorService's calculateMonthlyInstallment method is called exactly once
        verify(loanCalculatorService, times(1))
                .calculateMonthlyInstallment(anyString(), anyString(), anyInt(), anyDouble(), anyInt(), anyDouble());

        // Assert that the output stream is either empty (no errors) or contains a newline character (standard output)
        assertTrue(outputStream.toString().isEmpty() || outputStream.toString().contains("\n"));
    }

    @Test
    void testProcessInput_FileNotFound() {
        String invalidFilePath = "non_existent_input.txt";

        // Execute file processing with a file path that does not exist
        fileInputProcessor.processFile(invalidFilePath);
        // Assert that the output stream contains the error message for file not found
        assertTrue(outputStream.toString().contains(ErrorMessages.COULD_NOT_READ_FILE + invalidFilePath));

        // Verify that loanCalculatorService is never called, as file processing should halt before service invocation
        verify(loanCalculatorService, never()).calculateMonthlyInstallment(anyString(), anyString(), anyInt(), anyDouble(), anyInt(), anyDouble());
    }

    @Test
    void testProcessInput_InvalidFileFormat() throws IOException {
        String invalidFormatFilePath = "invalid_format_input.txt";
        String content = "invalid,format"; // Invalid format - less than 6 lines of input
        Files.write(Paths.get(invalidFormatFilePath), content.getBytes());
        fileInputProcessor.processFile(invalidFormatFilePath);

        // Assert that the output stream contains the error message for invalid file format (less than 6 lines)
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_FILE_FORMAT_SIX_LINES));
        Files.deleteIfExists(Paths.get(invalidFormatFilePath)); // Clean up the test file after test execution

        // Verify that loanCalculatorService is never called due to format validation failure
        verify(loanCalculatorService, never()).calculateMonthlyInstallment(anyString(), anyString(), anyInt(), anyDouble(), anyInt(), anyDouble());
    }

    @Test
    void testProcessInput_InvalidVehicleType() throws IOException {
        String invalidDataFilePath = "invalid_data_input.txt";
        String content = "invalid_type\nnew\n2024\n100000\n5\n10000"; // 'invalid_type' is not a valid vehicle type
        Files.write(Paths.get(invalidDataFilePath), content.getBytes());
        fileInputProcessor.processFile(invalidDataFilePath);

        // Assert that the output stream contains the error message for invalid vehicle type
        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_VEHICLE_TYPE));

        Files.deleteIfExists(Paths.get(invalidDataFilePath)); // Clean up the test file

        // Verify that loanCalculatorService is never called due to data validation failure
        verify(loanCalculatorService, never()).calculateMonthlyInstallment(anyString(), anyString(), anyInt(), anyDouble(), anyInt(), anyDouble());
    }

    @Test
    void testGetDownPaymentAmount_Valid() {
        double downPayment = 25000000;

        // Calculate down payment amount using the method
        double downPaymentAmount = fileInputProcessor.getDownPaymentAmount(downPayment, new BigDecimal(100_000_000));
        // Assert that the calculated down payment amount is correctly 25,000,000
        assertEquals(25_000_000, downPaymentAmount);
        // Assert that the output stream is empty, indicating no errors or outputs during down payment calculation
        assertTrue(outputStream.toString().isEmpty());
    }

    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(validFilePath));
    }
}
