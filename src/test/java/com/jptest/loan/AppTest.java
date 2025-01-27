package com.jptest.loan;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeTags({"service", "validation", "processor"})
@SelectPackages({"com.jptest.loan"})
public class AppTest 
{
}
