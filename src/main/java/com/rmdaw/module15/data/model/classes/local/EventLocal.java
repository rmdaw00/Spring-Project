package com.rmdaw.module15.data.model.classes.local;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.rmdaw.module15.data.model.classes.Ticket;
import com.rmdaw.module15.data.model.interfaces.IEvent;

@Component
public class EventLocal implements IEvent, Comparable<EventLocal>{

	private long eventId = 0;
	
	@NotEmpty (message = "Title cannot be empty")
	private String eventTitle;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date eventDate;
	
	private Set<Ticket> tickets;


	public long getId() {
		return eventId;
	}
    
    public void setId(long id) {
    	eventId = id;
	}
    
    public String getTitle() {	
		return eventTitle;
	}
    public void setTitle(String title) {
    	eventTitle = title;
	}
    public Date getDate() {
		return eventDate;
	}
    public void setDate(Date date) {
    	eventDate = date;
	}

	@Override
	public String toString() {
		return "Event [eventID=" + eventId + ", eventTitle=" + eventTitle + ", eventDate=" + eventDate + "]";
	}

	@Override
	public int compareTo(EventLocal o) {
		if(eventDate.equals(o.getDate())) {
			return (int) (getId()-o.getId());
		}
		return this.eventDate.compareTo(o.getDate());
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventDate, eventId, eventTitle, tickets);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventLocal other = (EventLocal) obj;
		return Objects.equals(eventDate, other.eventDate) && eventId == other.eventId
				&& Objects.equals(eventTitle, other.eventTitle) && Objects.equals(tickets, other.tickets);
	}
	
	
	
    
    
}
