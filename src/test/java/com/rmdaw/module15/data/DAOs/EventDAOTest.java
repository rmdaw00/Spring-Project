package com.rmdaw.module15.data.DAOs;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.rmdaw.module15.data.model.classes.Event;
import com.rmdaw.module15.data.model.classes.local.EventLocal;
import com.rmdaw.module15.data.repositories.EventsRepository;
import com.rmdaw.module15.data.repositories.local.LocalMap;
import com.rmdaw.module15.data.repositories.local.LocalStorage;

@Execution(ExecutionMode.CONCURRENT)
@DisplayName("Event Data Access Object")
@ExtendWith(MockitoExtension.class)
class EventDAOTest {
	
	Event event1, event2, event3;
	EventLocal eventLocal1, eventLocal2, eventLocal3;
	Page<Event> events;
	List<EventLocal> eventsLocal = new ArrayList<EventLocal>();
	
	@Mock
	private EventsRepository eventRepo;
	@Mock
	private LocalStorage localStorage;
	@Mock
	private LocalMap localDB;
	
	private EventDAO eventDAO;
	
	@BeforeEach
	void testInit() {
		/**
		 * Here I was instantiating each of items individually
		 * In Repository testing I used @ParameterizedTest instead
		 */
		eventDAO = new EventDAO(eventRepo, localStorage);
		eventDAO.localDB = localDB;
		
		LocalDate ld1 = LocalDate.of(2020, 10, 20);
		LocalDate ld2 = LocalDate.of(2020, 3, 20);
		LocalDate ld3 = LocalDate.of(2021, 4, 8);
		
		event1 = new Event();
		event1.setDate(Date.from(ld1.atStartOfDay(ZoneId.of("America/Toronto"))
								.toInstant()));
		event1.setId(1);
		event1.setTitle("Dance Class");
		
		eventLocal1 = new EventLocal();
		eventLocal1.setDate(Date.from(ld1.atStartOfDay(ZoneId.of("America/Toronto"))
								.toInstant()));
		eventLocal1.setId(1);
		eventLocal1.setTitle("Dance Class");
		
		event2 = new Event();
		event2.setDate(Date.from(ld2.atStartOfDay(ZoneId.of("America/Toronto"))
								.toInstant()));
		event2.setId(2);
		event2.setTitle("Running Class");
		
		eventLocal2 = new EventLocal();
		eventLocal2.setDate(Date.from(ld2.atStartOfDay(ZoneId.of("America/Toronto"))
								.toInstant()));
		eventLocal2.setId(2);
		eventLocal2.setTitle("Running Class");
		
		
		event3 = new Event();
		event3.setDate(Date.from(ld3.atStartOfDay(ZoneId.of("America/Toronto"))
								.toInstant()));
		event3.setId(3);
		event3.setTitle("Cooking Class");
		
		eventLocal3 = new EventLocal();
		eventLocal3.setDate(Date.from(ld3.atStartOfDay(ZoneId.of("America/Toronto"))
								.toInstant()));
		eventLocal3.setId(3);
		eventLocal3.setTitle("Cooking Class");
		
		
		events = new PageImpl<Event>(Arrays.asList(event1, event2, event3));
		eventsLocal.addAll(Arrays.asList(eventLocal1, eventLocal2, eventLocal3));
	}
	
	@Nested
	class getEventById {
		@Test
		@DisplayName("Getting Event or Null by ID - extDB")
		void testGetEventByIdExternalDB() {
			//mocked behavior
			when(eventRepo.findById(99L)).thenReturn(Optional.empty());
			when(eventRepo.findById(1L)).thenReturn(Optional.of(event1));
			
			//Database source
			eventDAO.localDataSet = false;
			assertTrue(!eventDAO.localDataSet);
			
			//Case Event not found returning null
			assertNull(eventDAO.getEventById(99L), "Null Returned if nothing found");
			
			//Returning Event if found
			assertSame(event1, eventDAO.getEventById(1L), "Object Returned From DB");

			//Behavioral Testing
			verify(localDB, never()).getEvent(anyLong());
			verify(eventRepo, times(2)).findById(anyLong());
		}	
		
		@Test
		@DisplayName("Getting EventLocal or Null by ID - localDB")
		void testGetEventByIdInternalDB() {
			//Mocked Behavior
			when(localDB.getEvent(1L)).thenReturn(eventLocal1);
			when(localDB.getEvent(99L)).thenReturn(null);
			
			//Database source
			eventDAO.localDataSet = true;
			assertTrue(eventDAO.localDataSet);
			
			//Case Event not found returning null
			assertNull(eventDAO.getEventById(99L), "Null Returned if nothing found");
			
			//Returning Event if found
			assertSame(eventLocal1, eventDAO.getEventById(1L), "Object Returned From DB");
			
			//Behavioral Testing
			verify(localDB,times(2)).getEvent(anyLong());
			verify(eventRepo,  never()).findById(anyLong());
		}	
	}

	@Nested
	class testGetEventsByTitle {
		/**
		 * Tests don't test paging, cuz its repository responsibility
		 */

		
		@Test
		@DisplayName("Getting Event by Title - extDB")
		void testGetEventsByTitleExternal() {
			//Database source
			eventDAO.localDataSet = false;
			assertTrue(!eventDAO.localDataSet);
			
			//Mocks
			when(eventRepo.findEventByEventTitleLikeIgnoreCase(any(),any(Pageable.class)))
			.thenReturn(Arrays.asList(event1));
		
			when(eventRepo.findAll(any(Pageable.class)))
			.thenReturn(events);
			
			//Tests
			assertEquals(Arrays.asList(event1), eventDAO.getEventsByTitle("Dance",5,1), "filtered by title");
			assertEquals(Arrays.asList(event2,event1,event3), eventDAO.getEventsByTitle(null, 5,1));
			
			//Behavioral Testing
			verify(localDB,never()).getAllEvents();
			verify(eventRepo).findAll(any(Pageable.class));
			verify(eventRepo).findEventByEventTitleLikeIgnoreCase(any(),any());
		}
		
		@Test
		@DisplayName("Getting EventLocal by Title - localDB")
		void testGetEventsByTitleLocal() {
			//Database source
			eventDAO.localDataSet = true;
			assertTrue(eventDAO.localDataSet);
			
			//Mocked Behavior
			when(localDB.getAllEvents()).thenReturn(eventsLocal);
			
			//Test
			assertEquals(Arrays.asList(eventLocal1),
						eventDAO.getEventsByTitle("Dance",5,1),
						"Filtering by Title");
			assertEquals(Arrays.asList(eventLocal2,eventLocal1, eventLocal3),
						eventDAO.getEventsByTitle(null,5,1),
						"Getting all sorted");
			
			//Testing Local Paging cuz its implemented in DAO, while DB paging is in Repo
			assertEquals(Arrays.asList(eventLocal2,eventLocal1),
						eventDAO.getEventsByTitle(null,2,1),
						"Testing Local Paging");
			assertEquals(Arrays.asList(eventLocal3),
						eventDAO.getEventsByTitle(null,2,2),
						"Testing Local Paging");
			
			//Behavior Testing
			verify(localDB,times(4)).getAllEvents();
			verify(eventRepo,never()).findAll();
			verify(eventRepo,never()).findEventByEventTitleLikeIgnoreCase(any(),any());
			
		}	
		
	}



	@Nested
	class testUpdateEvent {
		/**
		 * Proper testing of the method was not implemented. 
		 * 
		 * I Just checked if @UpdateLocalDB aspect annotation above the method being testing
		 * was triggered or not by calling the method within the test
		 * 
		 * this just confirmed it didn't which makes using aspecting pretty handy
		 * and test friendly
		 */
		@Test
		void testUpdateEventExternal() {
			//Database source
			eventDAO.localDataSet = true;
			assertTrue(eventDAO.localDataSet);
			
			
			assertNull(eventDAO.updateEvent(event1));
			
		}	
	}
	
	

}
