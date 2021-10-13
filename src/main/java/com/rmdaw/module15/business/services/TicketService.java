package com.rmdaw.module15.business.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rmdaw.module15.data.DAOs.TicketDAO;
import com.rmdaw.module15.data.model.interfaces.IEvent;
import com.rmdaw.module15.data.model.interfaces.ITicket;
import com.rmdaw.module15.data.model.interfaces.IUser;
import com.rmdaw.module15.data.model.interfaces.ITicket.Category;

@Service
public class TicketService {

	private TicketDAO ticketDAO;

	public TicketService(TicketDAO ticketDAO) {
		super();
		this.ticketDAO = ticketDAO;
	}
	
	
	
	public ITicket bookTicket(long userId, long eventId, int place, Category category) {
		return ticketDAO.bookTicket(userId, eventId, place, category);
	}

	
	public List<ITicket> getBookedTickets(IUser user, int pageSize, int pageNum) {
		return ticketDAO.getBookedTickets(user, pageSize, pageNum);
	}

	
	public List<ITicket> getBookedTickets(IEvent event, int pageSize, int pageNum) {
		return ticketDAO.getBookedTickets(event, pageSize, pageNum);
	}

	
	public boolean cancelTicket(long ticketId) {
		return ticketDAO.cancelTicket(ticketId);
	}
	
	
}
