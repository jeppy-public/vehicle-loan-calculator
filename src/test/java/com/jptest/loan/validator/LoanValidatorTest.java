package com.jptest.loan.validator;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link LoanValidator}.
 * This test class is designed to verify the validation logic implemented
 * in the {@code LoanValidator} class. It includes tests for various input
 * validations such as year format, year comparison with current year,
 * loan amount, vehicle type, vehicle condition, and loan tenor.
 *
 * Each test method focuses on a specific validation rule, ensuring that
 * the {@code LoanValidator} correctly identifies valid and invalid inputs
 * for loan applications.
 */
@Tag("validation")
@ExtendWith(MockitoExtension.class)
class LoanValidatorTest {
    private final LoanValidator loanValidator = new LoanValidator();

    /**
     * Tests {@link LoanValidator#isValidYearFourDigit(int)} for valid input.
     * This test verifies that the validator correctly identifies a four-digit number
     * as a valid year format.
     */
    @Test
    void isValidYearFourDigit_Valid() {
        assertTrue(loanValidator.isValidYearFourDigit(2024));
    }

    /**
     * Tests {@link LoanValidator#isValidYearFourDigit(int)} for invalid input.
     * This test ensures that the validator rejects a two-digit number as an invalid
     * year format, expecting a four-digit year.
     */
    @Test
    void isValidYearFourDigit_Invalid() {
        assertFalse(loanValidator.isValidYearFourDigit(24));
    }

    /**
     * Tests {@link LoanValidator#isValidYearCompareWithCurrentYear(int)} for valid input.
     * This test checks if the validator accepts a past year as valid, as vehicle year
     * should not be in the future relative to the current year.
     */
    @Test
    void isValidYearCompareWithCurrentYear_Valid() {
        assertTrue(loanValidator.isValidYearCompareWithCurrentYear(2023));
    }

    /**
     * Tests {@link LoanValidator#isValidYearCompareWithCurrentYear(int)} for invalid input.
     * This test verifies that the validator rejects a future year, ensuring that
     * vehicle year is not set in the future.
     */
    @Test
    void isValidYearCompareWithCurrentYear_Invalid() {
        assertFalse(loanValidator.isValidYearCompareWithCurrentYear(2026));
    }

    /**
     * Tests {@link LoanValidator#isValidLoanAmount(double)} with a valid loan amount.
     * This test ensures that the validator accepts a loan amount within the valid range.
     */
    @Test
    void isValidLoanAmount_Valid() {
        assertTrue(loanValidator.isValidLoanAmount(1000));
    }

    /**
     * Tests {@link LoanValidator#isValidLoanAmount(double)} with an invalid zero loan amount.
     * This test verifies that the validator rejects zero as a loan amount,
     * as loan amounts must be positive.
     */
    @Test
    void isValidLoanAmount_Invalid_Zero() {
        assertFalse(loanValidator.isValidLoanAmount(0));
    }

    /**
     * Tests {@link LoanValidator#isValidLoanAmount(double)} with a negative loan amount.
     * This test ensures that the validator rejects negative values as invalid
     * loan amounts, which are not logically possible.
     */
    @Test
    void isValidLoanAmount_Invalid_Negative() {
        assertFalse(loanValidator.isValidLoanAmount(-1000));
    }

    /**
     * Tests {@link LoanValidator#isValidLoanAmount(double)} with an excessively large loan amount.
     * This test checks if the validator correctly identifies and rejects loan amounts
     * that exceed the maximum allowed limit.
     */
    @Test
    void isValidLoanAmount_Invalid_TooLarge() {
        assertFalse(loanValidator.isValidLoanAmount(1_000_000_001));
    }

    /**
     * Tests {@link LoanValidator#isValidVehicleType(String)} with a valid vehicle type "car".
     * This test verifies that the validator recognizes "car" as a valid vehicle type.
     */
    @Test
    void isValidVehicleType_Valid_Car() {
        assertTrue(loanValidator.isValidVehicleType("car"));
    }

    /**
     * Tests {@link LoanValidator#isValidVehicleType(String)} with a valid vehicle type "motorcycle".
     * This test ensures that the validator correctly identifies "motorcycle"
     * as a valid vehicle type option.
     */
    @Test
    void isValidVehicleType_Valid_Motorcycle() {
        assertTrue(loanValidator.isValidVehicleType("motorcycle"));
    }

    /**
     * Tests {@link LoanValidator#isValidVehicleType(String)} with an invalid vehicle type "truck".
     * This test checks if the validator rejects "truck", which is not a supported
     * vehicle type in the loan application.
     */
    @Test
    void isValidVehicleType_Invalid() {
        assertFalse(loanValidator.isValidVehicleType("truck"));
    }

    /**
     * Tests {@link LoanValidator#isValidVehicleCondition(String)} with a valid condition "new".
     * This test verifies that the validator accepts "new" as a valid vehicle condition.
     */
    @Test
    void isValidVehicleCondition_Valid_New() {
        assertTrue(loanValidator.isValidVehicleCondition("new"));
    }

    /**
     * Tests {@link LoanValidator#isValidVehicleCondition(String)} with a valid condition "old".
     * This test ensures the validator recognizes "old" as a valid vehicle condition.
     */
    @Test
    void isValidVehicleCondition_Valid_Old() {
        assertTrue(loanValidator.isValidVehicleCondition("old"));
    }

    /**
     * Tests {@link LoanValidator#isValidVehicleCondition(String)} with an invalid condition "used".
     * This test checks if the validator rejects "used" as it is not a supported
     * vehicle condition for loan applications.
     */
    @Test
    void isValidVehicleCondition_Invalid() {
        assertFalse(loanValidator.isValidVehicleCondition("used"));
    }

    /**
     * Tests {@link LoanValidator#isValidLoanTenor(int)} with a valid loan tenor.
     * This test verifies that the validator accepts a loan tenor within the allowed range.
     */
    @Test
    void isValidLoanTenor_Valid() {
        assertTrue(loanValidator.isValidLoanTenor(3));
    }

    /**
     * Tests {@link LoanValidator#isValidLoanTenor(int)} with an invalid, too short loan tenor.
     * This test ensures that the validator rejects a loan tenor that is too short,
     * specifically zero years, as invalid.
     */
    @Test
    void isValidLoanTenor_Invalid_TooShort() {
        assertFalse(loanValidator.isValidLoanTenor(0));
    }

    /**
     * Tests {@link LoanValidator#isValidLoanTenor(int)} with an invalid, too long loan tenor.
     * This test checks if the validator correctly identifies and rejects loan tenors
     * that exceed the maximum allowed duration.
     */
    @Test
    void isValidLoanTenor_Invalid_TooLong() {
        assertFalse(loanValidator.isValidLoanTenor(7));
    }
}
