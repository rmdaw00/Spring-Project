package com.rmdaw.module15.data.model.classes;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.rmdaw.module15.data.model.interfaces.IEvent;

@Component
@Scope("prototype")
@Entity
@Table(name="EVENTS")
public class Event implements IEvent, Comparable<Event>{

	@Id
	@Column(name="eventid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long eventId = 0;
	
	@Column(name="eventtitle")
	private String eventTitle;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="eventdate")
	private Date eventDate;
	
	@OneToMany(mappedBy = "event",fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
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
	public int compareTo(Event o) {
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
		Event other = (Event) obj;
		return Objects.equals(eventDate, other.eventDate) && eventId == other.eventId
				&& Objects.equals(eventTitle, other.eventTitle) && Objects.equals(tickets, other.tickets);
	}
	
	
	
    
    
}
