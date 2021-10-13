package com.rmdaw.module15.data.model.classes.local;


import org.springframework.stereotype.Component;

import com.rmdaw.module15.data.model.interfaces.ITicket;

@Component
public class TicketLocal implements ITicket {
	
    public enum Category {STANDARD, PREMIUM, BAR}
    

    private long ticketId;
    
    @Override
	public String toString() {
		return "Ticket [ticketID=" + ticketId + ", eventID=" + eventID + ", event=" + event + ", userID=" + userID
				+ ", user=" + user + ", ticketCategory=" + ticketCategory + ", ticketPlace=" + ticketPlace + "]";
	}

    private long eventID = 0;
    
	private EventLocal event;
   
  
    public TicketLocal() {
    	
    }
    
    public TicketLocal(UserLocal user,EventLocal event, int ticketPlace, Category ticketCategory) {
		super();
		this.user = user;
		userID = user.getId();
		eventID = event.getId();
		this.event = event;
		this.ticketCategory = ticketCategory;
		this.ticketPlace = ticketPlace;
	}

	public EventLocal getEvent() {
		return event;
	}

	public void setEvent(EventLocal event) {
		this.event = event;
	}

	public UserLocal getUser() {
		return user;
	}

	public void setUser(UserLocal user) {
		this.user = user;
	}

    private long userID;
    

	private UserLocal user;


    private Category ticketCategory = Category.STANDARD;

    private int ticketPlace;
   
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
	public void setCategory(ITicket.Category category) {
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
