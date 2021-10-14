package com.rmdaw.module15.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import com.rmdaw.module15.data.model.classes.collections.local.Tickets;
import com.rmdaw.module15.data.model.classes.local.EventLocal;
import com.rmdaw.module15.data.model.classes.local.TicketLocal;
import com.rmdaw.module15.data.model.classes.local.UserLocal;
import com.rmdaw.module15.data.model.interfaces.ITicket;

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


	 @Bean
	 public Jaxb2Marshaller getJax2Marshaller() {
		 Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		 marshaller.setClassesToBeBound(
			 //ITicket.class,
			 TicketLocal.class,
			 Tickets.class);
		 
		 
		 Map<String, Object> properties = new HashMap<>();
		 properties.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
		 marshaller.setMarshallerProperties(properties);

		 return marshaller;
	 }
	 
	
}
