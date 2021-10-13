package com.rmdaw.module15.data.repositories.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.rmdaw.module15.aspects.Loggable;
import com.rmdaw.module15.data.model.classes.local.EventLocal;
import com.rmdaw.module15.data.model.classes.local.TicketLocal;
import com.rmdaw.module15.data.model.classes.local.UserLocal;

/**
 * 
 * LocalMap is a custom hashmap that stores 3 classes as objects
 * has custom methods of retrieving 
 * 
 * @author Rashed_Dawabsheh
 *
 */

public class LocalMap extends HashMap<String, Object>{
	@JsonIncludeProperties("idCount")
	private int idCount = 1;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param id
	 * @return Event object from LocalMap
	 */
	@Loggable
	public EventLocal getEvent(long id) {
		try {
			return (EventLocal)get("event:"+id);
		} catch (Exception e) {
			return null; 
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return User object from LocalMap
	 */
	
	public UserLocal getUser(long id) {
		try {
			return (UserLocal)get("user:"+id);
		} catch (Exception e) {
			return null; 
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return Ticket object from LocalMap
	 */
	public TicketLocal getTicket(long id) {
		try {
			return (TicketLocal)get("ticket:"+id);
		} catch (Exception e) {
			return null; 
		}
	}
	
	
	public List<TicketLocal> getAllTickets() {
		List<TicketLocal> tickets = new ArrayList<>();
		
		keySet().forEach(key -> {
			if (key.substring(0,7).equals("ticket:")) {
				TicketLocal ticket = (TicketLocal) get(key);
				ticket.setEvent(getEvent(ticket.getEventId()));
				ticket.setUser(getUser(ticket.getUserId()));
				tickets.add(ticket);

			}
		});
		
		return tickets;
	}
	
	public List<EventLocal> getAllEvents() {
		List<EventLocal> events = new ArrayList<>();
		
		keySet().forEach(key -> {
			if (key.substring(0,6).equals("event:")) {
				EventLocal event = (EventLocal) get(key);
				events.add(event);
			}
		});
		
		return events;
	}
	
	public List<UserLocal> getAllUsers() {
		List<UserLocal> users = new ArrayList<>();
		
		keySet().forEach(key -> {
			if (key.substring(0,5).equals("user:")) {
				UserLocal user = (UserLocal) get(key);
				users.add(user);
			}
		});
		
		return users;
	}
	
	public TicketLocal putTicket(TicketLocal ticket) {
		if (ticket.getId() == 0) ticket.setId(idCount++);
		put("ticket:"+ticket.getId(), ticket);
		
		return ticket;
	}
	
	public Boolean removeTicket(long id) {
		return (null!=remove("ticket:"+ id));
	}
	
	public EventLocal putEvent(EventLocal event) {

		if (event.getId() == 0) event.setId(idCount++);
		put("event:"+event.getId(), event);	
		
		return event;
	}

	public Boolean removeEvent(long id) {
		return (null!=remove("event:"+ id));
			
	}
	public UserLocal putUser(UserLocal user) {
		if (user.getId() == 0) user.setId(idCount++);
		put("user:"+user.getId(), user);
		
		return user;
	} 
	
	public Boolean removeUser(long id) {
		return (null!=remove("user:"+ id));
	}
	
	public int getIdCount() {
		return idCount;
	}


	public void setIdCount(int idCount) {
		this.idCount = idCount;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(idCount);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocalMap other = (LocalMap) obj;
		return idCount == other.idCount;
	}
}
