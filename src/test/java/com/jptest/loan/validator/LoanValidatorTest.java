package com.jptest.loan.validator;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link LoanValidator}.
 * This class contains test methods to verify the validation logic for loan inputs.
 */
@Tag("validation")
@ExtendWith(MockitoExtension.class)
class LoanValidatorTest {
    @Mock
    private LoanValidator loanValidator;

    /**
     * Test case for {@link LoanValidator#isValidYearFourDigit(int)} for valid four-digit year.
     * Verifies that a valid four-digit year returns false, indicating it is not invalid.
     */
    @Test
    void isValidYearFourDigit_Valid() {
        assertFalse(loanValidator.isValidYearFourDigit(2024));
    }

    /**
     * Test case for {@link LoanValidator#isValidYearFourDigit(int)} for invalid two-digit year.
     * Verifies that an invalid two-digit year returns true, indicating it is invalid.
     */
    @Test
    void isValidYearFourDigit_Invalid() {
        assertTrue(loanValidator.isValidYearFourDigit(24));
    }

    /**
     * Test case for {@link LoanValidator#isValidYearCompareWithCurrentYear(int)} for valid past year.
     * Verifies that a valid past year returns false, indicating it is not invalid.
     */
    @Test
    void isValidYearCompareWithCurrentYear_Valid() {
        assertFalse(loanValidator.isValidYearCompareWithCurrentYear(2023));
    }

    /**
     * Test case for {@link LoanValidator#isValidYearCompareWithCurrentYear(int)} for invalid future year.
     * Verifies that an invalid future year returns true, indicating it is invalid.
     */
    @Test
    void isValidYearCompareWithCurrentYear_Invalid() {
        assertFalse(loanValidator.isValidYearCompareWithCurrentYear(2026));
    }

    /**
     * Test case for {@link LoanValidator#isValidLoanAmount(double)} for valid loan amount.
     * Verifies that a valid loan amount returns false, indicating it is not invalid.
     */
    @Test
    void isValidLoanAmount_Valid() {
        assertFalse(loanValidator.isValidLoanAmount(1000));
    }

    /**
     * Test case for {@link LoanValidator#isValidLoanAmount(double)} for invalid zero loan amount.
     * Verifies that an invalid zero loan amount returns true, indicating it is invalid.
     */
    @Test
    void isValidLoanAmount_Invalid_Zero() {
        assertFalse(loanValidator.isValidLoanAmount(0));
    }

    /**
     * Test case for {@link LoanValidator#isValidLoanAmount(double)} for invalid negative loan amount.
     * Verifies that an invalid negative loan amount returns true, indicating it is invalid.
     */
    @Test
    void isValidLoanAmount_Invalid_Negative() {
        assertFalse(loanValidator.isValidLoanAmount(-1000));
    }

    /**
     * Test case for {@link LoanValidator#isValidLoanAmount(double)} for invalid too large loan amount.
     * Verifies that an invalid too large loan amount returns true, indicating it is invalid.
     */
    @Test
    void isValidLoanAmount_Invalid_TooLarge() {
        assertFalse(loanValidator.isValidLoanAmount(1_000_000_001));
    }

    /**
     * Test case for {@link LoanValidator#isValidVehicleType(String)} for valid vehicle type "car".
     * Verifies that a valid vehicle type "car" returns false, indicating it is not invalid.
     */
    @Test
    void isValidVehicleType_Valid_Car() {
        assertFalse(loanValidator.isValidVehicleType("car"));
    }

    /**
     * Test case for {@link LoanValidator#isValidVehicleType(String)} for valid vehicle type "motorcycle".
     * Verifies that a valid vehicle type "motorcycle" returns false, indicating it is not invalid.
     */
    @Test
    void isValidVehicleType_Valid_Motorcycle() {
        assertFalse(loanValidator.isValidVehicleType("motorcycle"));
    }

    /**
     * Test case for {@link LoanValidator#isValidVehicleType(String)} for invalid vehicle type "truck".
     * Verifies that an invalid vehicle type "truck" returns true, indicating it is invalid.
     */
    @Test
    void isValidVehicleType_Invalid() {
        assertFalse(loanValidator.isValidVehicleType("truck"));
    }

    /**
     * Test case for {@link LoanValidator#isValidVehicleCondition(String)} for valid vehicle condition "new".
     * Verifies that a valid vehicle condition "new" returns false, indicating it is not invalid.
     */
    @Test
    void isValidVehicleCondition_Valid_New() {
        assertFalse(loanValidator.isValidVehicleCondition("new"));
    }

    /**
     * Test case for {@link LoanValidator#isValidVehicleCondition(String)} for valid vehicle condition "old".
     * Verifies that a valid vehicle condition "old" returns false, indicating it is not invalid.
     */
    @Test
    void isValidVehicleCondition_Valid_Old() {
        assertFalse(loanValidator.isValidVehicleCondition("old"));
    }

    /**
     * Test case for {@link LoanValidator#isValidVehicleCondition(String)} for invalid vehicle condition "used".
     * Verifies that an invalid vehicle condition "used" returns true, indicating it is invalid.
     */
    @Test
    void isValidVehicleCondition_Invalid() {
        assertTrue(loanValidator.isValidVehicleCondition("used"));
    }

    /**
     * Test case for {@link LoanValidator#isValidLoanTenor(int)} for valid loan tenor.
     * Verifies that a valid loan tenor returns true, indicating it is valid.
     */
    @Test
    void isValidLoanTenor_Valid() {
        assertTrue(loanValidator.isValidLoanTenor(3));
    }

    /**
     * Test case for {@link LoanValidator#isValidLoanTenor(int)} for invalid too short loan tenor.
     * Verifies that an invalid too short loan tenor returns false, indicating it is invalid.
     */
    @Test
    void isValidLoanTenor_Invalid_TooShort() {
        assertFalse(loanValidator.isValidLoanTenor(0));
    }

    /**
     * Test case for {@link LoanValidator#isValidLoanTenor(int)} for invalid too long loan tenor.
     * Verifies that an invalid too long loan tenor returns false, indicating it is invalid.
     */
    @Test
    void isValidLoanTenor_Invalid_TooLong() {
        assertFalse(loanValidator.isValidLoanTenor(7));
    }
}
