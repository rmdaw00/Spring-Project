package com.rmdaw.module15;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class SpringCucuContext {
	@Bean
	public WebDriver webDriver() {
	    return new ChromeDriver();
	}
}
