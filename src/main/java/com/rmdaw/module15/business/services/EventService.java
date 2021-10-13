package com.rmdaw.module15.business.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rmdaw.module15.data.DAOs.EventDAO;
import com.rmdaw.module15.data.model.interfaces.IEvent;

@Service
public class EventService {
	
	
	private final EventDAO eventDAO;
	
	@Autowired
	public EventService(EventDAO eventDAO) {
		this.eventDAO = eventDAO;
	}
	
	public IEvent getEventById(long eventId) {
		return eventDAO.getEventById(eventId);
	}
	
	public List<IEvent> getEventsByTitle(String title, int pageSize, int pageNum) {
		return eventDAO.getEventsByTitle(title, pageSize, pageNum);
	}
	
	public List<IEvent> getEventsForDay(Date day, int pageSize, int pageNum) {
		return eventDAO.getEventsForDay(day, pageSize, pageNum);
	}
	
	
	public IEvent createEvent(IEvent event) {
		return eventDAO.createEvent(event);
	}
	
	
	public IEvent updateEvent(IEvent event) { 
		return eventDAO.updateEvent(event);
	}
	
	
	public boolean deleteEvent(long eventId) { 
		return eventDAO.deleteEvent(eventId);
	}
	
}
