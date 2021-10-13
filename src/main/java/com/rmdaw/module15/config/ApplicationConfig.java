package com.rmdaw.module15.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@Configuration
@ComponentScan(basePackages = "com.rmdaw.module15")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy 
public class ApplicationConfig extends WebMvcConfigurationSupport {
	
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		//super.addResourceHandlers(registry); //XXX should I keep?
		registry.addResourceHandler("css/**")
				.addResourceLocations("classpath:static/css/");
	}
	
	 @Bean
	    public Java8TimeDialect java8TimeDialect() {
	        return new Java8TimeDialect();
	  }


	 
	
}
