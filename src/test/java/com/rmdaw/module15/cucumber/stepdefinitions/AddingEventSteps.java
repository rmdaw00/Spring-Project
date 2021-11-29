package com.rmdaw.module15.cucumber.stepdefinitions;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties.Storage;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.rmdaw.module15.SpringCucuContext;
import com.rmdaw.module15.data.repositories.EventsRepository;
import com.rmdaw.module15.data.repositories.local.LocalMap;
import com.rmdaw.module15.data.repositories.local.LocalStorage;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@CucumberContextConfiguration
@ContextConfiguration(classes = SpringCucuContext.class)
@AutoConfigureMockMvc
public class AddingEventSteps {

	
	@Value("${app.localDataSet}")
	private boolean localDataSet;	
	
	private EventsRepository eventRepo;
	
	private LocalMap localDB;
	private ResultActions apiCall;
	private String senarioTitle;
	private String senarioDate;
	private int initialCount;
	
	
	@Autowired
	private WebDriver driver;  
	private int justCount = 0; 
	/*
	 * 
	 * Note here that scenarios behave like JUnit test, they are ran independent though they all 
	 * rely on the spring context that has a state, LocalDB is in memory DB that is persisted 
	 * throughout the scenario because its tied to spring application context, while justCount 
	 * is not thus each scenario sees a new justCount. meaning different objects of addingEventSteps 
	 * were used to test each scenario differently
	 */
	
	@Autowired
	private MockMvc mockMvc;
	
	public AddingEventSteps(LocalStorage localStorage, EventsRepository eventRepo) {
		System.setProperty("webdriver.chrome.driver", "C:\\RMD\\ChromeDriver\\chromedriver.exe");
		this.eventRepo = eventRepo;
		localDB = localStorage.getDatabase();
		initialCount = localDB.getAllEvents().size();
	}
	

	
	@Given("title is {string}")
	public void title_is(String title) {
		senarioTitle = title;
	}
	@Given("date is {string}")
	public void date_is(String date) {
		senarioDate = date;
	}
	@Given("storage is {string}")
	public void storage_is_local(String storageType) {
		//localDataSet = storageType.equalsIgnoreCase("local");
		switch (storageType) {
		case "local":
			Assumptions.assumeTrue(localDataSet);
			break;
		case "db":
			Assumptions.assumeFalse(localDataSet);
			break;
		default:
			throw new IllegalArgumentException();
		}
		
	}


	@When("User Adds Event using API")
	public void user_adds_event_using_api() throws Exception {
		System.out.printf("String is %s  %n"
				+ "Date is %s  %n"
				+ "Count is: %s  %n", 
				senarioTitle,senarioDate,
				initialCount=localDB.getAllEvents().size());
		apiCall = mockMvc.perform(post("/api/events")
									.param("title", senarioTitle)
									.param("date", senarioDate));
		

	}
	
	@When("User Adds Event using Website")
	public void user_adds_event_using_website() {
		System.out.printf("String is %s %n"
				+ "Date is %s %n"
				+ "Count is: %s %n", 
				senarioTitle,senarioDate,
				initialCount=localDB.getAllEvents().size());


		
		
		driver = new ChromeDriver();
		driver.get("http://localhost:8080/events/create");
		
		WebElement titleInputBox = driver.findElement(By.id("title"));
		WebElement dateInputBox = driver.findElement(By.id("date"));
		WebElement submitButton = driver.findElement(By.cssSelector("input.btn-primary[type=\"submit\"]"));
		
		titleInputBox.sendKeys(senarioTitle);
		dateInputBox.sendKeys(senarioDate);
		
		submitButton.click();
	}
	
	
	//DB EFFECT
	@Then("count increased")
	public void count_increased() {
		assertEquals(initialCount+1, localDB.getAllEvents().size());
	}
	
	@Then("count is same")
	public void count_is_same() {
		 assertEquals(initialCount, localDB.getAllEvents().size());
	}

	
	//WEBSITE RESULTS
	@Then("Event persisted sent to other page")
	public void event_persisted_sent_to_other_page() {
	    WebElement errorElement = driver.findElement(By.cssSelector(".alert span"));
		
		assertTrue(errorElement.getText().contains("Event Created Successfully"));
		
		driver.quit();
	}
	
	@Then("back to page with error")
	public void back_to_page_with_error() {
		WebElement errorElement = driver.findElement(By.cssSelector(".alert span"));
		
		assertTrue(errorElement.getText().contains("Error in the form"));
		
		driver.quit();
	}

	
	
	
	
	//API RESULTs
	@Then("getting error status")
	public void getting_error_status() throws Exception {
		apiCall.andExpect(status().isBadRequest());
	}
	
	@Then("Event persisted with status created")
	public void event_persisted_with_status_created() throws Exception {
		//Date date = new Date();
		
		//LocalDate ld = LocalDate.now();
		
		String result = apiCall.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			//.andExpect(jsonPath("$.date").value(0L))
			.andReturn().getResponse().getContentAsString();
		
		
	     apiCall.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.title").value(senarioTitle));
	}

}
