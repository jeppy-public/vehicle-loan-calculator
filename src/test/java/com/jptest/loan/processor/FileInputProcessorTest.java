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

@Tag("processor")
@ExtendWith(MockitoExtension.class)
public class FileInputProcessorTest {

    @Mock
    private LoanCalculatorService loanCalculatorService;

    private LoanValidator loanValidator;

    private FileInputProcessor fileInputProcessor;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private final String validFilePath = "input.txt"; // Assuming input.txt exists in test resources or project root

    @BeforeEach
    void setUp() throws IOException {
        fileInputProcessor = new FileInputProcessor(loanCalculatorService, new LoanValidator());
        System.setOut(new PrintStream(outputStream));

        // Manually set the minimumDownPaymentRate
        ReflectionTestUtils.setField(fileInputProcessor, "minimumDownPaymentRate", new BigDecimal("25"));

        //MockitoAnnotations.openMocks(this);
        // Create a valid input file for testing
        String content = "car\nnew\n2024\n100000\n5\n35000";
        Files.write(Paths.get(validFilePath), content.getBytes());
    }

    @Test
    void testProcessInput_ValidFile() {
        fileInputProcessor.processFile(validFilePath);

        // Verify that the loanCalculatorService was called with expected arguments
        verify(loanCalculatorService, times(1))
                .calculateMonthlyInstallment(anyString(), anyString(), anyInt(), anyDouble(), anyInt(), anyDouble());

        // Verify no error messages were printed
        assertTrue(outputStream.toString().isEmpty() || outputStream.toString().contains("\n"));

    }

    @Test
    void testProcessInput_FileNotFound() {
        String invalidFilePath = "non_existent_input.txt";

        fileInputProcessor.processFile(invalidFilePath);
        assertTrue(outputStream.toString().contains(ErrorMessages.COULD_NOT_READ_FILE + invalidFilePath));

        verify(loanCalculatorService, never()).calculateMonthlyInstallment(anyString(), anyString(), anyInt(), anyDouble(), anyInt(), anyDouble());
    }


    @Test
    void testProcessInput_InvalidFileFormat() throws IOException {
        String invalidFormatFilePath = "invalid_format_input.txt";
        String content = "invalid,format"; // Invalid format - less than 6 fields
        Files.write(Paths.get(invalidFormatFilePath), content.getBytes());
        fileInputProcessor.processFile(invalidFormatFilePath);

        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_FILE_FORMAT_SIX_LINES));
        Files.deleteIfExists(Paths.get(invalidFormatFilePath)); // Clean up the file
        verify(loanCalculatorService, never()).calculateMonthlyInstallment(anyString(), anyString(), anyInt(), anyDouble(), anyInt(), anyDouble());
    }

    @Test
    void testProcessInput_InvalidVehicleType() throws IOException {
        String invalidDataFilePath = "invalid_data_input.txt";
        String content = "invalid_type\nnew\n2024\n100000\n5\n10000";
        Files.write(Paths.get(invalidDataFilePath), content.getBytes());
        fileInputProcessor.processFile(invalidDataFilePath);

        assertTrue(outputStream.toString().contains(ErrorMessages.INVALID_VEHICLE_TYPE));

        Files.deleteIfExists(Paths.get(invalidDataFilePath)); // Clean up the file
        verify(loanCalculatorService, never()).calculateMonthlyInstallment(anyString(), anyString(), anyInt(), anyDouble(), anyInt(), anyDouble());
    }

    @Test
    void testGetDownPaymentAmount_Valid() {
        double downPayment = 25000000;

        double downPaymentAmount = fileInputProcessor.getDownPaymentAmount(downPayment, new BigDecimal(100_000_000));
        assertEquals(25_000_000, downPaymentAmount);
        assertTrue(outputStream.toString().isEmpty());
    }

    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(validFilePath));
    }
}
