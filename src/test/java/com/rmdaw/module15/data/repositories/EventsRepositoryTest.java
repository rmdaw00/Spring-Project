package com.rmdaw.module15.data.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.rmdaw.module15.data.model.classes.Event;

@DataJpaTest
class EventsRepositoryTest {

	@Autowired
	EventsRepository underTest;
	
	Event event;
	
	List<Event> events;
	
	
	@BeforeEach
	void initDB() {
		Event event1, event2, event3;
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
		
		events = Arrays.asList(event1, event2, event3);
		events.forEach(underTest::save);

	
	}
	
	@Nested
	class testFindEventByEventTitleLikeIgnoreCase {
		
		Pageable pageable ;
		List<Event> result;
		
		
		@BeforeEach
		void initInit() {
			pageable = PageRequest.of(0, 10);
		}
		
		@Test
		void testInitProper() { 
			assertEquals(3, underTest.count());
		}
		
		@Test
		void testExactCase() { 
			result = underTest.findEventByEventTitleLikeIgnoreCase("%Class%", pageable);
			assertEquals(3, result.size());
		}
		
		@Test
		void testDIfferentCase() { 
			result = underTest.findEventByEventTitleLikeIgnoreCase("%cLass%", pageable);
			assertEquals(3, result.size());
		}
		
		@Test
		void testOneResult() { 
			result = underTest.findEventByEventTitleLikeIgnoreCase("%Run%", pageable);
			assertEquals(1, result.size());
		}
		//Testing Anti Logic
		@Test
		void testFindNothing() { 
			result = underTest.findEventByEventTitleLikeIgnoreCase("%scLass%", pageable);
			assertEquals(0, result.size());
		}

		//
		@Test
		@DisplayName("Testing Edge Cases: Zero DB")
		void testZero() { 
			underTest.deleteAll();
			assertEquals(0, underTest.count());
			result = underTest.findEventByEventTitleLikeIgnoreCase("%Run%", pageable);
			assertEquals(0, result.size());
		}
	}

	@Nested
	class testFindAllPageable {
		Pageable pageable;
		Page<Event> result;
		
		@BeforeEach
		void initinit() {
			pageable = PageRequest.of(0, 10);
			result = underTest.findAll(pageable);
		}
		
		@Test
		void testFindAll() {
		//Testing Logic
			result = underTest.findAll(pageable);
			assertEquals(3L, result.getTotalElements());
		}
		
		@Test
		void testPageable() {
		//Testing Paging
			pageable = PageRequest.of(1, 2);
			result = underTest.findAll(pageable);
			assertEquals(1L, result.toList().size());
		}
		
		@Test
		void testZero() {
		//Testing Edge Cases: Zero DB
			underTest.deleteAll();
			result = underTest.findAll(pageable);
			assertEquals(0L, result.getTotalElements());
		}
		//Testing Throwables
	}

	@Nested
	class testFindEventByEventDate {
		
		LocalDate ld3;
		Date date;
		Pageable pageable;
		List<Event> result;

		@BeforeEach
		void initInit() {
			pageable = PageRequest.of(0, 10);
			ld3 = LocalDate.of(2021, 4, 8);
			date = Date.from(ld3.atStartOfDay(ZoneId.of("America/Toronto")).toInstant());
		}
		
		
		@Test
		@DisplayName("Testing Finding Date")
		void testFindingDate() { 
			result = underTest.findEventByEventDate(date, pageable);
			assertEquals(2, result.size());
		}
	
		@Test
		@DisplayName("Testing Paging")
		void testPagin() { 
			pageable = PageRequest.of(1, 1);
			result = underTest.findEventByEventDate(date, pageable);
			assertEquals(1, result.size());
		}
		
		
		@Test
		@DisplayName("Testing Finding Nothing")
		void testFindingNothing() { 
			ld3 = LocalDate.of(2021, 4, 9);
			date = Date.from(ld3.atStartOfDay(ZoneId.of("America/Toronto")).toInstant());
			result = underTest.findEventByEventDate(date, pageable);
			assertEquals(0, result.size());
		}
		
		@Test
		@DisplayName("Testing Edge Cases: Zero DB")
		void testZero() { 
			underTest.deleteAll();
			result = underTest.findEventByEventDate(date, pageable);
			assertEquals(0, result.size());
		}
		
		//Testing Throwables
	}

}
