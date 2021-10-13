package com.rmdaw.module15.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rmdaw.module15.aspects.Loggable;
import com.rmdaw.module15.business.facade.BookingFacadeImplementation;
import com.rmdaw.module15.data.model.classes.Event;
import com.rmdaw.module15.data.model.classes.local.EventLocal;
import com.rmdaw.module15.data.model.interfaces.IEvent;
import com.rmdaw.module15.web.DateUtils;

@RestController
@RequestMapping("/api/events")
public class EventsRestController {
	
	private BookingFacadeImplementation facade;
	private static final String EVENT_NOT_FOUND = "Event not found";

	@Value("${app.localDataSet}")
	private Boolean local;
	
	@Autowired
	public EventsRestController(BookingFacadeImplementation facade) {
		this.facade = facade;
	}
	
	@Loggable
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<IEvent>> getEventsByTitle(
			@RequestParam(value="title",required=false) String title, 
			@RequestParam(value="pageSize", required = false, defaultValue = "20") int pageSize, 
			@RequestParam(value="page", required = false, defaultValue="1") int page) {
		List<IEvent> events =  facade.getEventsByTitle(title, pageSize, page);
		
		if (!events.isEmpty()) {
			return new ResponseEntity<>(events,HttpStatus.FOUND);
		} else {
			return new ResponseEntity<>(events,HttpStatus.NOT_FOUND);
		}
	}
	


	@Loggable
	@GetMapping(value="/date", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<IEvent>> getEventsByDate(
			@RequestParam(value="date",required = false) String date, 
			@RequestParam(value="pageSize", required = false, defaultValue = "20") int pageSize, 
			@RequestParam(value="page", required = false, defaultValue="1") int page) {
		List<IEvent> events = facade.getEventsForDay(DateUtils.createDateFromDateString(date),pageSize, page);
		
		if (!events.isEmpty()) {
			return new ResponseEntity<>(events,HttpStatus.FOUND);
		} else {
			return new ResponseEntity<>(events,HttpStatus.NOT_FOUND);
		}
		
		
	}
	
	@Loggable
	@GetMapping(params = "eventId")
	public ResponseEntity<IEvent> getEventByID(@RequestParam(value="eventId") long eventId) {
		IEvent event =  facade.getEventById(eventId);
		
		if (event !=null){
			return new ResponseEntity<>(event, HttpStatus.FOUND);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, EVENT_NOT_FOUND);
		} 
	}

	
	@Loggable
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IEvent> createEvent(
			@RequestParam(value="title") String title, 
			@RequestParam(value="date",required = false) String date) {
		
		IEvent event = Boolean.TRUE.equals(local)? new EventLocal():new Event();
		event.setId(0);
		event.setTitle(title);
		event.setDate(DateUtils.createDateFromDateString(date));
		
		return new ResponseEntity<>(facade.createEvent(event),HttpStatus.CREATED);
	}
	
	@Loggable
	@PostMapping(params = "eventId", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IEvent> updateEvent(
			@RequestParam(value="eventId") long eventId,
			@RequestParam(value="title") String title, 
			@RequestParam(value="date") String date) {
		
		
		IEvent event = Boolean.TRUE.equals(local)? new EventLocal():new Event();
		event.setId(eventId);
		event.setTitle(title);
		event.setDate(DateUtils.createDateFromDateString(date));
		
		IEvent result = facade.updateEvent(event);
		
		if (result!=null) {
			return new ResponseEntity<>(result,HttpStatus.OK);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, EVENT_NOT_FOUND);
		}
	}
	
	@Loggable
	@DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteEventByID(@RequestParam(value="eventId") long eventId) {
		if (facade.deleteEvent(eventId)) {
			return new ResponseEntity<>("Deleted",HttpStatus.OK);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, EVENT_NOT_FOUND);
		}
	}
	

}
