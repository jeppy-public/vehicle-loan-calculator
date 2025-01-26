package com.jptest.loan.processor;

import com.jptest.loan.service.LoanCalculatorService;
import com.jptest.loan.validator.LoanValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Tag("processor")
@ExtendWith(MockitoExtension.class)
public class FileInputProcessorTest {

    @Mock
    private LoanCalculatorService loanCalculatorService;

    @Mock
    private LoanValidator loanValidator;

    @InjectMocks
    private FileInputProcessor fileInputProcessor;

    @BeforeEach
    void setUp() {
        // fileInputProcessor is now injected with mocks
    }

    @Test
    void testProcessInput() {
        Path tempFile;
        try {
            tempFile = Files.createTempFile("prefix", ".txt");
            Files.writeString(tempFile, "Test content");

            fileInputProcessor.processFile(tempFile.toAbsolutePath().toString());
            // Use the file in your test
            Files.delete(tempFile); // Clean up after test
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testProcessInput_validFile() throws IOException {
        Path tempFile = Files.createTempFile("test_input", ".txt");
        Files.writeString(tempFile, "car\nnew\n2023\n100000\n5\n35000"); // Valid input

        FileInputProcessor fileInputProcessor = new FileInputProcessor(loanCalculatorService, loanValidator);
        fileInputProcessor.processFile(tempFile.toAbsolutePath().toString());

        ArgumentCaptor<String> vehicleTypeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> vehicleConditionCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> vehicleYearCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Double> loanAmountCaptor = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Integer> loanTenorCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Double> downPaymentCaptor = ArgumentCaptor.forClass(Double.class);


        Mockito.verify(loanCalculatorService).calculateMonthlyInstallment(
                vehicleTypeCaptor.capture(),
                vehicleConditionCaptor.capture(),
                vehicleYearCaptor.capture(),
                loanAmountCaptor.capture(),
                loanTenorCaptor.capture(),
                downPaymentCaptor.capture()
        );

        assertEquals("car", vehicleTypeCaptor.getValue());
        assertEquals("new", vehicleConditionCaptor.getValue());
        assertEquals(2023, vehicleYearCaptor.getValue());
        assertEquals(100000.0, loanAmountCaptor.getValue());
        assertEquals(5, loanTenorCaptor.getValue());
        assertEquals(35000.0, downPaymentCaptor.getValue());
    }

    @Test
    void testProcessInput_invalidFile() throws IOException {
        Path tempFile = Files.createTempFile("invalid_input", ".txt");
        Files.writeString(tempFile, "invalid\nnew\n2023\n100000\n5\n35000"); // Invalid vehicle type

        FileInputProcessor fileInputProcessor = new FileInputProcessor(loanCalculatorService, loanValidator);
        when(loanValidator.isValidVehicleType(Mockito.anyString())).thenReturn(true); // Mock invalid vehicle type

        fileInputProcessor.processFile(tempFile.toAbsolutePath().toString());

        Mockito.verify(loanValidator).isValidVehicleType(Mockito.anyString());
        // TODO: Add assertions to check for validation errors, e.g., exceptions or error messages (print statements)
    }
}
