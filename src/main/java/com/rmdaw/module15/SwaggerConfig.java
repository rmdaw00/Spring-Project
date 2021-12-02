package com.rmdaw.module15;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket restAPIsDocket() {
		return new Docket(DocumentationType.OAS_30)
				.groupName("rest")
				.apiInfo(restApiInfo())
				.select()
				.paths( PathSelectors.ant("/api/**")) 
		        .apis(RequestHandlerSelectors.basePackage("com.rmdaw.module15"))
				.build();
	}
	
	@Bean
	public Docket mvcAPIsDocket() {
		return new Docket(DocumentationType.OAS_30)
				.groupName("mvc")
				.apiInfo(mvcApiInfo())
				.select()
				.paths(PathSelectors.ant("/api/**").negate()) 
		        .apis(RequestHandlerSelectors.basePackage("com.rmdaw.module15"))
				.build();
	}
	
	@Bean
	public ApiInfo restApiInfo() {
		return new ApiInfoBuilder()
				.title("Event Booking App API Documentation")
			     .version("1.0.2")
			     .description("This is the REST API components of Event Booking App, the app ")
			     .license("Apache 2.0")
			     .licenseUrl("/license.html")
			     .termsOfServiceUrl("/terms2021.html")
			     .contact(new Contact("PhoneBook", "contact/", "contact@eventbooking.com"))   
				.build();
	}
	
	@Bean
	public ApiInfo mvcApiInfo() {
		return new ApiInfoBuilder()
				.title("Event Booking App MVC Documentation")
			     .version("1.0.2")
			     .description("This is the MVC controllers components of Event Booking App, the app ")
			     .license("Apache 2.0")
			     .licenseUrl("/license.html")
			     .termsOfServiceUrl("/terms2021.html")
			     .contact(new Contact("PhoneBook", "contact/", "contact@eventbooking.com"))   
				.build();
	}
}
