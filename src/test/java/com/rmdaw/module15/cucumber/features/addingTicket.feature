#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
Feature: Adding An Event 
	@WebsiteSucceed
  Scenario: Using WEBSITE to SUCCESSFULLY add events
    Given title is <title>
    	And date is <date>
    	And storage is <storageType>
    When User Adds Event using Website
 		Then Event persisted sent to other page
 		Then count increased

	Examples:
	|title				|date					|storageType	|
	|"some event"	|"2020-1035"	|"db"					|
	|"some event"	|"2020-1035"	|"local"			|
	|"some event"	|"2020-10-20"	|"db"					|
	|"some event"	|"2020-10-20"	|"local"			|
	
	
	@WebsiteFail
  Scenario: Using WEBSITE to FAIL add events
    Given title is <title>
    	And date is <date>
    	And storage is <storageType>
    When User Adds Event using Website
 		Then back to page with error
 		 And count is same 

	Examples:
	|title				|date					|storageType	|
	|""						|"2020-10-20"	|"db"					|
	|""						|"2020-10-20"	|"local"			|
	


 @APIFail
  Scenario: Using API to FAIL add events
    Given title is <title>
    	And date is <date>
    	And storage is <storageType>
    When User Adds Event using API
 		Then getting error status
 		 And count is same

	Examples:
	|title				|date					|storageType	|
	|""						|"2020-10-20"	|"db"					|
	|""						|"2020-10-20"	|"local"			|
    
 @APISucceed
  Scenario: Using API to SUCCESSFULLY add events 
    Given title is <title>
    	And date is <date>
    	And storage is <storageType>
    When User Adds Event using API
 		Then Event persisted with status created
 		 And count increased

	Examples:
	|title				|date					|storageType	|
	|"some event"	|"2020-1035"	|"db"					|
	|"some event"	|"2020-1035"	|"local"			|
	|"some event"	|"2020-10-20"	|"db"					|
	|"some event"	|"2020-10-20"	|"local"			| 
    
    
