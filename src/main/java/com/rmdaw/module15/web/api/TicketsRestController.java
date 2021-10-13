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
import com.rmdaw.module15.data.model.classes.User;
import com.rmdaw.module15.data.model.classes.local.EventLocal;
import com.rmdaw.module15.data.model.classes.local.UserLocal;
import com.rmdaw.module15.data.model.interfaces.IEvent;
import com.rmdaw.module15.data.model.interfaces.ITicket;
import com.rmdaw.module15.data.model.interfaces.IUser;

@RestController
@RequestMapping("/api/tickets")
public class TicketsRestController {
	
	private BookingFacadeImplementation facade;
	private static final String TICKET_NOT_FOUND = "Ticket not found";

	
	@Value("${app.localDataSet}")
	private Boolean local;
	
	
	@Autowired
	public TicketsRestController(BookingFacadeImplementation facade) {
		this.facade = facade;
	}
	
	
	@Loggable
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ITicket> bookTicket(
			@RequestParam(value="userId") long userId,
			@RequestParam(value="eventId") long eventId, 
			@RequestParam(value="place") int place,
			@RequestParam(value="category") String category) {

		ITicket ticket = facade.bookTicket(userId, eventId, place, ITicket.Category.valueOf(category));
		
		if (ticket == null) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Place already taken");
		} else {
			return new ResponseEntity<>(ticket, HttpStatus.CREATED);
		}
	}
	
	@Loggable
	@GetMapping(params= {"userId"},produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ITicket>> getBookedTicketsByUser(
			@RequestParam(value="userId") long userId, 
			@RequestParam(value="pageSize", required = false, defaultValue = "20") int pageSize, 
			@RequestParam(value="page", required = false, defaultValue="1") int page) {
		
		IUser user = Boolean.TRUE.equals(local)?new UserLocal():new User();
		user.setId(userId);
		
		List<ITicket> tickets = facade.getBookedTickets(user, pageSize, page);
		if (!tickets.isEmpty()) {
			return new ResponseEntity<>(tickets,HttpStatus.FOUND);	
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, TICKET_NOT_FOUND);
		}
	}
	
	@Loggable
	@GetMapping(params= {"eventId"},produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ITicket>> getBookedTicketsByEvent(
			@RequestParam(value="eventId") long eventId, 
			@RequestParam(value="pageSize", required = false, defaultValue = "20") int pageSize, 
			@RequestParam(value="page", required = false, defaultValue="1") int page) {
		
		IEvent event= Boolean.TRUE.equals(local)?new EventLocal():new Event();
		event.setId(eventId);
		
		List<ITicket> tickets = facade.getBookedTickets(event, pageSize, page);
		if (tickets!=null) {
			return new ResponseEntity<>(tickets,HttpStatus.FOUND);	
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, TICKET_NOT_FOUND);
		}
	}
	
	
	@Loggable
	@DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> cancelTicketByID(@RequestParam(value="ticketId") long ticketId) {
		if (facade.cancelTicket(ticketId)) {
			return new ResponseEntity<>("Deleted",HttpStatus.OK);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, TICKET_NOT_FOUND);
		}
	}
}
