# Vehicle Loan Calculator Application

## Description
This is a simple command-line application built with Java and Spring Boot to calculate the monthly installment for vehicle loans. The application supports two input modes: manual input via the console and file input. It validates loan parameters such as vehicle type, condition, year, loan amount, and loan tenor to ensure accurate calculations.

## Tech Stack
- Java 17
- Spring Boot 3.4.1
- Maven 3.9.9
- JUnit 5.11.4

## How to Run

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Maven 3.6.0 or higher

### Steps to Run
1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd vehicle-loan-calculator
   ```
   *(Replace `<repository-url>` with the actual repository URL)*

2. **Build the application using Maven:**
   ```bash
   mvn clean install
   ```

3. **Build Docker Image:**
   ```bash
   docker build -t vehicle-loan-calculator .
   ```

4. **Run Docker Image:**
   ```bash
   docker run -it --rm vehicle-loan-calculator
   ```

   This will start the Vehicle Loan Calculator application.

## How to Install

As a command-line application, installation primarily involves building the application from source. No traditional installation steps are required beyond having the prerequisites installed.

1. **Ensure Prerequisites:** Make sure you have JDK 17+ and Maven installed and configured on your system.
2. **Build the Application:** Follow the "How to Run" steps to clone and build the application.
3. **Executable JAR:** After building, an executable JAR file (`vehicle-loan-calculator-1.0-SNAPSHOT.jar`) will be created in the `target` directory. This JAR file is self-contained and can be run on any system with Java installed.

## How to Run Test Cases

To execute the unit tests for the application, use the following Maven command:

```bash
mvn test
```

This command will run all test cases located in the `src/test/java` directory and provide a summary of the test results.

## How to Operate the Application

The application supports two modes of operation:

### 1. Manual Input Mode (Console)
- When you run the application without any arguments, it defaults to manual input mode.
- The application will prompt you to enter loan details step-by-step in the console: vehicle type, vehicle condition, vehicle year, loan amount, loan tenor, and down payment.
- Follow the prompts and enter the required information.
- The application will then calculate and display the monthly installment.

### 2. File Input Mode
- You can provide input via a text file named `input.txt` placed in the root directory of the project.
- The `input.txt` file should contain loan details in the following order, each on a new line:
    ```
    <vehicle_type>
    <vehicle_condition>
    <vehicle_year>
    <loan_amount>
    <loan_tenor>
    <down_payment>
    ```
    *(Replace placeholders with actual values)*
- To run the application in file input mode, ensure the `input.txt` file is correctly formatted and present in the project root directory when you start the application.

## Application Rules

- **Vehicle Types:** Supported vehicle types are "car" and "motorcycle".
- **Vehicle Conditions:** Supported conditions are "new" and "old".
- **Vehicle Year:** Must be a four-digit year and not in the future.
- **Loan Amount:** Must be a positive value and not exceed 1,000,000,000.
- **Loan Tenor:** Must be between 1 and 6 years.
- **Down Payment:** Must be a valid positive number. Minimum down payment rate is 25% of the loan amount.

---
**Note:** This README provides a comprehensive guide to understanding, running, testing, and operating the Vehicle Loan Calculator application.
