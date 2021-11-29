package com.rmdaw.module15.cucumber.testrunner;

import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@Suite
@SelectClasspathResource("com/rmdaw/module15/cucumber/features")
public class CucumberRunner {

	
}
