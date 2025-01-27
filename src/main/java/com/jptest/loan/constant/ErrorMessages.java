package com.jptest.loan.constant;

/**
/**
 * {@code ErrorMessages} class to hold error message constants.
 * <p>This class defines all the error messages used in the application.
 * It provides constants for various validation and file handling error scenarios.</p>
 */
public final class ErrorMessages {

    /**
     * Generic error prefix for all error messages.
     */
    public static final String ERROR = "\nError: ";
    /**
     * Error message for invalid vehicle type input.
     */
    public static final String INVALID_VEHICLE_TYPE = ERROR + "Invalid vehicle type. Must be 'car' or 'motorcycle'.";
    /**
     * Error message for invalid vehicle condition input.
     */
    public static final String INVALID_VEHICLE_CONDITION = ERROR + "Invalid vehicle condition. Must be 'new' or 'old'.";
    /**
     * Error message for invalid vehicle condition with year.
     */
    public static final String INVALID_VEHICLE_CONDITION_WITH_YEAR = ERROR + "Year for 'NEW' vehicle cannot be less than current year - 1 ";
    /**
     * Error message for invalid year format (not 4 digits).
     */
    public static final String INVALID_YEAR_4_DIGIT = ERROR + "Invalid year. Must be a 4-digit number.";
    /**
     * Error message for invalid vehicle year compared to the current year.
     */
    public static final String INVALID_YEAR_COMPARE_CURRENT_YEAR = ERROR + "Invalid vehicle year. Must be less or equals than current year.";
    /**
     * Error message for invalid loan amount input.
     */
    public static final String INVALID_LOAN_AMOUNT = ERROR + "Invalid amount. Must be a numeric value not exceeding 1 billion.";
    /**
     * Error message for invalid loan tenor input.
     */
    public static final String INVALID_TENOR = ERROR + "Invalid loan tenor. Must be between 1 and 6 years.";
    /**
     * Error message for invalid input format in general.
     */
    public static final String INVALID_INPUT_FORMAT = ERROR + "Invalid input format. Please enter numbers for year, loan amount, tenor, and down payment.";
    /**
     * Error message for invalid file format (not six lines).
     */
    public static final String INVALID_FILE_FORMAT_SIX_LINES = ERROR + "Invalid file format. File must contain 6 lines of input.";
    /**
     * Error message for invalid down payment amount (below minimum).
     */
    public static final String INVALID_DOWN_PAYMENT_AMOUNT = ERROR + "Down payment below allowable minimum.";
    /**
     * Error message for file reading failure.
     */
    public static final String COULD_NOT_READ_FILE = ERROR + "Could not read file: ";

}
