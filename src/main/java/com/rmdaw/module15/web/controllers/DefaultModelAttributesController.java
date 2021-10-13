package com.rmdaw.module15.web.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.rmdaw.module15.business.facade.BookingFacadeImplementation;
import com.rmdaw.module15.data.model.classes.Event;
import com.rmdaw.module15.data.model.classes.Ticket;
import com.rmdaw.module15.data.model.classes.User;
import com.rmdaw.module15.data.model.classes.local.EventLocal;
import com.rmdaw.module15.data.model.classes.local.TicketLocal;
import com.rmdaw.module15.data.model.classes.local.UserLocal;
import com.rmdaw.module15.data.model.interfaces.IEvent;
import com.rmdaw.module15.data.model.interfaces.ITicket;
import com.rmdaw.module15.data.model.interfaces.IUser;

@Service
@ControllerAdvice
public class DefaultModelAttributesController {
	
	private BookingFacadeImplementation facade;
	
	public DefaultModelAttributesController(BookingFacadeImplementation facade) {
		super();
		this.facade = facade;
	}

	@Value("${app.localDataSet}")
	private Boolean local;
	
	@ModelAttribute("event")
	public IEvent getEvent() {
		return Boolean.TRUE.equals(local)?new EventLocal():new Event();
	}
	
	@ModelAttribute("user")
	public IUser getUser() {
		return Boolean.TRUE.equals(local)?new UserLocal():new User();
	}
	
	@ModelAttribute("ticket")
	public ITicket getTicket() {
		return Boolean.TRUE.equals(local)?new TicketLocal():new Ticket();
	}
	
	@ModelAttribute("today")
	public Date getToday() {
		return new Date();
	}
	
	@ModelAttribute("categories")
	public List<String> getCategories() {
		return Stream.of(ITicket.Category.values())
				.map(String::valueOf)
				.collect(Collectors.toList());
	}
	
	@ModelAttribute("users")
	public Map<Long, String> getUsers() {
		List<IUser> users = facade.getUsersByName("", 1000, 1); //XXX: 1000 not scalable
		
		return users.stream()
				.collect(Collectors.toMap(IUser::getId, IUser::getName));
	}
	
	@ModelAttribute("events")
	public Map<Long, String> getEvents() {
		List<IEvent> events = facade.getEventsByTitle("", 1000, 1); //XXX: 1000 not scalable
		
		return events.stream()
				.collect(Collectors.toMap(IEvent::getId, IEvent::getTitle));
	}
}
