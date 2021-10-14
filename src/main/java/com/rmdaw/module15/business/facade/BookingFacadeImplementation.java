package com.rmdaw.module15.business.facade;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rmdaw.module15.business.services.EventService;
import com.rmdaw.module15.business.services.TicketService;
import com.rmdaw.module15.business.services.UserService;
import com.rmdaw.module15.data.model.interfaces.IEvent;
import com.rmdaw.module15.data.model.interfaces.ITicket;
import com.rmdaw.module15.data.model.interfaces.IUser;
import com.rmdaw.module15.data.model.interfaces.ITicket.Category;

@Service
public class BookingFacadeImplementation implements BookingFacade{

	private EventService eventService;
	private UserService userService;
	private TicketService ticketService;
	
	@Autowired
	public BookingFacadeImplementation(EventService eventService, UserService userService, TicketService ticketService) {
		super();
		this.eventService = eventService;
		this.userService = userService;
		this.ticketService = ticketService;
	}
	
	public IEvent getEventById(long eventId) {
		return eventService.getEventById(eventId);
	}

	@Override
	public List<IEvent> getEventsByTitle(String title, int pageSize, int pageNum) {
		return eventService.getEventsByTitle(title, pageSize, pageNum);
	}

	@Override
	public List<IEvent> getEventsForDay(Date day, int pageSize, int pageNum) {
		return eventService.getEventsForDay(day, pageSize, pageNum);
	}

	@Override
	public IEvent createEvent(IEvent event) {
		return eventService.createEvent(event);
	}

	@Override
	public IEvent updateEvent(IEvent event) {
		return eventService.updateEvent(event);
	}

	@Override
	public boolean deleteEvent(long eventId) {
		return eventService.deleteEvent(eventId);
	}

	@Override
	public IUser getUserById(long userId) {

		return userService.getUserById(userId);
	}

	@Override
	public IUser getUserByEmail(String email) {
		return userService.getUserByEmail(email);
	}

	@Override
	public List<IUser> getUsersByName(String name, int pageSize, int pageNum) {
		return userService.getUsersByName(name, pageSize, pageNum);
	}

	@Override
	public IUser createUser(IUser user) {
		return userService.createUser(user);
	}

	@Override
	public IUser updateUser(IUser user) {
		return userService.updateUser(user);
	}

	@Override
	public boolean deleteUser(long userId) {
		return userService.deleteUser(userId);
	}

	@Override
	public ITicket bookTicket(long userId, long eventId, int place, Category category) {
		return ticketService.bookTicket(userId, eventId, place, category);
	}

	@Override
	public List<ITicket> getBookedTickets(IUser user, int pageSize, int pageNum) {
		return ticketService.getBookedTickets(user, pageSize, pageNum);
	}

	@Override
	public List<ITicket> getBookedTickets(IEvent event, int pageSize, int pageNum) {
		return ticketService.getBookedTickets(event, pageSize, pageNum);
	}

	@Override
	public boolean cancelTicket(long ticketId) {
		return ticketService.cancelTicket(ticketId);
	}
	
	@Override
	public Map<ITicket, Boolean> loadBatchTickets(List<ITicket> tickets) { 
		return ticketService.loadBatchTickets(tickets);
	}
	
}
