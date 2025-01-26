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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Tag("processor")
@ExtendWith(MockitoExtension.class)
public class ManualInputProcessorTest {

    @Mock
    private LoanCalculatorService loanCalculatorService;

    @Mock
    private LoanValidator loanValidator;

    @InjectMocks
    private ManualInputProcessor manualInputProcessor;

    @BeforeEach
    void setUp() {
        // manualInputProcessor is now injected with mocks
    }

    @Test
    void testProcessInput_validInput() {
        String input = "car\nnew\n2023\n100000\n5\n35000\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        manualInputProcessor.processInput();

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

        System.setIn(System.in); // Reset to standard input
    }
}
