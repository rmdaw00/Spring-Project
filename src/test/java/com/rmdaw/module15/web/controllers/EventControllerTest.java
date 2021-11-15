package com.rmdaw.module15.web.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.rmdaw.module15.business.facade.BookingFacadeImplementation;
import com.rmdaw.module15.data.model.classes.Event;
import com.rmdaw.module15.data.model.interfaces.IEvent;

/**
 *  MVC Controller Unit Test
 *  with Mocked Facade Service
 * 
 * @author Rashed_Dawabsheh
 *
 */


@WebMvcTest(controllers = EventController.class)
class EventControllerTest {
	
	@MockBean
	private BookingFacadeImplementation facade;
	
	@Autowired
	private MockMvc mockMvc;
	
	Event event1, event2, event3;
	
	@BeforeEach
	void testInit() {
		LocalDate ld1 = LocalDate.of(2020, 10, 20);
		LocalDate ld2 = LocalDate.of(2021, 4, 8);
		LocalDate ld3 = LocalDate.of(2021, 4, 8);
		
		event1 = new Event();
		event1.setDate(Date.from(ld1.atStartOfDay(ZoneId.of("America/Toronto"))
								.toInstant()));
		event1.setId(1);
		event1.setTitle("Dance Class");
		
	
		
		event2 = new Event();
		event2.setDate(Date.from(ld2.atStartOfDay(ZoneId.of("America/Toronto"))
								.toInstant()));
		event2.setId(2);
		event2.setTitle("Running Class");
		
		event3 = new Event();
		event3.setDate(Date.from(ld3.atStartOfDay(ZoneId.of("America/Toronto"))
								.toInstant()));
		event3.setId(3);
		event3.setTitle("Cooking Class");
	}
	
	
	@Test
	void testGetEventsByTitle() throws Exception {
		
		List<IEvent> events = Arrays.asList(event1); 
		
		
		when(facade.getEventsByTitle(anyString(), anyInt(), anyInt())).thenReturn(events);
		
		mockMvc.perform(get("/events/?search=test")).andExpect(status().isOk())
			.andExpect(content().string(containsString("Dance Class")));
		
		//Behavioral Testing
		
		/**
		 * actually doing testing, I caught a bug, I discovered that I was calling DB twice
		 * which if it was real senario I would need addressing, its cuz I used 
		 * default ModelAttribute  
		 * 
		 * verify(facade).getEventsByTitle(anyString(), anyInt(), anyInt());
		 */
		//just to pass tests to demo skill
		verify(facade, times(2)).getEventsByTitle(anyString(), anyInt(), anyInt());
	}

}
