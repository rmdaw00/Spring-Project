package com.rmdaw.module15.data.model.classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.rmdaw.module15.data.model.interfaces.ITicket;

@Component
@Entity
@Transactional
@Table(name="tickets")
public class Ticket implements ITicket {
    
    @Id
    @Column(name = "ticketid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketId = 0;
    
    @Override
	public String toString() {
		return "Ticket [ticketID=" + ticketId + ", eventID=" + eventID + ", event=" + event + ", userID=" + userID
				+ ", user=" + user + ", ticketCategory=" + ticketCategory + ", ticketPlace=" + ticketPlace + "]";
	}

	@Column(name = "eventid")
    private long eventID;
    
    @ManyToOne
    @JoinColumn(name = "eventid",insertable = false,updatable = false)
	private Event event;
    
	@Column(name = "userid")
    private long userID;
    
    @ManyToOne
    @JoinColumn(name = "userid",insertable = false,updatable = false)
	private User user;
    
	@Column(name="ticketcategory")
    @Enumerated(EnumType.ORDINAL)
    private Category ticketCategory;
	
    public enum Category {STANDARD, PREMIUM, BAR}
	
    @Column(name="ticketplace")
    private int ticketPlace;
   
  
    public Ticket() {
    	
    }
    
    public Ticket(long userID,long eventID, int ticketPlace, Category ticketCategory) {
		super();
		this.eventID = eventID;
		this.userID = userID;
		this.ticketCategory = ticketCategory;
		this.ticketPlace = ticketPlace;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	@Override
	public long getId() {
		return ticketId;
	}

	@Override
	public void setId(long id) {
		ticketId = id;
	}
	
	@Override
	public long getEventId() {
		return eventID;
	}


	@Override
	public void setEventId(long eventId) {
		eventID = eventId;
	}

	@Override
	public long getUserId() {
		return userID;
	}

	@Override
	public void setUserId(long userId) {
		userID = userId;
	}

	@Override
	public ITicket.Category getCategory() {
		return (ITicket.Category.values()[ticketCategory.ordinal()]) ;
	}

	@Override
	public void setCategory(com.rmdaw.module15.data.model.interfaces.ITicket.Category category) {
		ticketCategory = Category.values()[category.ordinal()];
		
	}

	@Override
	public int getPlace() {
		return ticketPlace;
	}

	@Override
	public void setPlace(int place) {
		ticketPlace = place;
		
	}
    

}
