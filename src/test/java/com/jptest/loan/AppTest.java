package com.jptest.loan;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeTags({"service", "validation", "processor"})
@SelectPackages({"com.jptest.loan"})
/**
 * {@code AppTest} is a JUnit Platform Suite that aggregates and executes
 * tests from different parts of the application.
 * It is configured to include tests tagged with "service", "validation", and "processor",
 * and it selects packages under "com.jptest.loan" for test discovery.
 *
 * This class serves as an entry point for running integration or suite tests,
 * ensuring that tests across different modules (service, validation, processor)
 * are executed together in a cohesive manner.
 */
public class AppTest
{
}
