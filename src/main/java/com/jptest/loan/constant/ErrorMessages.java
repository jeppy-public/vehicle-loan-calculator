package com.jptest.loan.constant;

public final class ErrorMessages {

    public static final String ERROR = "\nError: ";
    public static final String INVALID_VEHICLE_TYPE = ERROR + "Invalid vehicle type. Must be 'car' or 'motorcycle'.";
    public static final String INVALID_VEHICLE_CONDITION = ERROR + "Invalid vehicle condition. Must be 'new' or 'old'.";
    public static final String INVALID_VEHICLE_CONDITION_WITH_YEAR = ERROR + "Year for 'NEW' vehicle cannot be less than current year - 1 ";
    public static final String INVALID_YEAR_4_DIGIT = ERROR + "Invalid year. Must be a 4-digit number.";
    public static final String INVALID_YEAR_COMPARE_CURRENT_YEAR = ERROR + "Invalid vehicle year. Must be less or equals than current year.";
    public static final String INVALID_LOAN_AMOUNT = ERROR + "Invalid amount. Must be a numeric value not exceeding 1 billion.";
    public static final String INVALID_TENOR = ERROR + "Invalid loan tenor. Must be between 1 and 6 years.";
    public static final String INVALID_INPUT_FORMAT = ERROR + "Invalid input format. Please enter numbers for year, loan amount, tenor, and down payment.";
    public static final String INVALID_FILE_FORMAT_SIX_LINES = ERROR + "Invalid file format. File must contain 6 lines of input.";
    public static final String INVALID_DOWN_PAYMENT_AMOUNT = ERROR + "Down payment below allowable minimum.";
    public static final String COULD_NOT_READ_FILE = ERROR + "Could not read file: ";

}
