package com.rmdaw.module15.data.DAOs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.rmdaw.module15.aspects.UpdateLocalDB;
import com.rmdaw.module15.data.model.classes.Ticket;
import com.rmdaw.module15.data.model.classes.local.TicketLocal;
import com.rmdaw.module15.data.model.interfaces.IEvent;
import com.rmdaw.module15.data.model.interfaces.ITicket;
import com.rmdaw.module15.data.model.interfaces.ITicket.Category;
import com.rmdaw.module15.data.model.interfaces.IUser;
import com.rmdaw.module15.data.repositories.TicketsRepository;
import com.rmdaw.module15.data.repositories.local.LocalStorage;

@Component
public class TicketDAO extends CommonDAO{

	private TicketsRepository ticketRepo;
	
	@Autowired
	public TicketDAO(TicketsRepository ticketRepo, LocalStorage localStorage) {
		super(localStorage);
		this.ticketRepo = ticketRepo;
	}

	// Get all booked tickets for specified user. 
	// sorted by event date in descending order.
	public List<ITicket> getBookedTickets(IUser user, int pageSize, int pageNum) {
		int currentPage = pageNum-PAGING_OFFSET;
		List<ITicket> tickets = new ArrayList<>();
		
		if (!localDataSet) {
			Pageable pageable = PageRequest.of(currentPage, pageSize);
			ticketRepo.findTicketsByUserID(user.getId(), pageable).forEach(tickets::add);
			return tickets;
		} else {
			//Local Storage
			List<TicketLocal> ticketsDB = localDB.getAllTickets();
			ticketsDB.stream()
				.filter(ticket -> {
					if (ticket.getUser() != null) {
						return ticket.getUser().getId()==user.getId();
					} else {
						return false;
					}
				})
				.sorted((t1,t2)-> {
					if (null==t1.getEvent()) return 1;
					if (null==t2.getEvent()) return -1;
					return t2.getEvent().getDate().compareTo(t1.getEvent().getDate());
				})
				.skip((long)pageSize*currentPage)
				.limit(pageSize)
				.forEach(tickets::add);
				
			
			return tickets;			
		}
		
	}

	// Get all booked tickets for specified event. 
	// sorted in by user email in ascending order.	
	public List<ITicket> getBookedTickets(IEvent event, int pageSize, int pageNum) {
		int currentPage = pageNum-PAGING_OFFSET;
		List<ITicket> tickets = new ArrayList<>();
		
		if (!localDataSet) {
			//DATABASE
			Pageable pageable = PageRequest.of(currentPage, pageSize);
			ticketRepo.findTicketsByEventID(event.getId(), pageable).forEach(tickets::add);
			
			return tickets;
		} else {
			List<TicketLocal> ticketsDB = localDB.getAllTickets();
			ticketsDB.stream()
				.filter(ticket -> {
					if (ticket.getEvent()!=null) {
						return ticket.getEvent().getId()==event.getId();
					} else {
						return true;
					}
				})
				.sorted((t1,t2)-> {
					if (null==t1.getUser()) return 1;
					if (null==t2.getUser()) return -1;
					return t1.getUser().getEmail().compareTo(t2.getUser().getEmail());
				})
				.skip((long)pageSize*currentPage)
				.limit(pageSize)
				.forEach(tickets::add);
			return tickets;		
		}
		
	}
	
	@UpdateLocalDB
	public ITicket bookTicket(long userId, long eventId, int place, Category category) {
		
		if (!localDataSet) {
			//DATABASE
			
			
			if (!ticketRepo.findByEventIDAndTicketPlace(eventId, place).isEmpty()) {
				//Place is taken
				return null;
			} else {
				Ticket ticket = new Ticket(userId, eventId, place, 
						Ticket.Category.values()[category.ordinal()]);
				return ticketRepo.save(ticket);
			}
			
		} else {
			//Local Storage
			if(localDB.getAllTickets().stream()
					.anyMatch(ticket -> (ticket.getPlace()==place 
									&& ticket.getEventId()== eventId))) {
				//Place is taken
				return null;
			} else {
				TicketLocal ticket =  new TicketLocal(localDB.getUser(userId), 
										localDB.getEvent(eventId), place, 
										TicketLocal.Category.values()[category.ordinal()]);
				return localDB.putTicket(ticket);
			}
		}
	}
	

	@UpdateLocalDB
	public boolean cancelTicket(long ticketId) {
		if (!localDataSet) {
			//DATABASE
			Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
			if (ticketOpt.isPresent()) {
				Ticket ticket = ticketOpt.get();
				ticketRepo.delete(ticket);
				return true;
			} else {
				return false;
			}			
		} else {
			//Local Storage
			return localDB.removeTicket(ticketId);
		}
		
	}
	
	@UpdateLocalDB
	public Map<ITicket, Boolean> loadBatchTickets(List<ITicket> tickets) {
		Map<ITicket, Boolean> results = new HashMap<>();
		if (!localDataSet) {
			//DATABASE
			tickets.forEach(ticket -> {
				ticket.setId(0); //to make sure they are treated as new
				results.put(ticket, ticketRepo.save((Ticket)ticket)==null);
			});
		} else {
			//Local Storage
			tickets.forEach(ticket -> {
				ticket.setId(0); //to make sure they are treated as new
				if(localDB.getAllTickets().stream()
						.anyMatch(t -> (ticket.getPlace()==t.getPlace() 
										&& ticket.getEventId()==t.getEventId()))) {
					//Place is taken
					results.put(ticket,false);
				} else {
					
					if (localDB.getEvent(ticket.getEventId())==null || 
							localDB.getUser(ticket.getUserId())==null) {
						results.put(ticket,false);
					} else {
					//saving ticket locally
					localDB.putTicket(new TicketLocal(localDB.getUser(ticket.getUserId()), 
							localDB.getEvent(ticket.getEventId()), ticket.getPlace(), 
							TicketLocal.Category.values()[ticket.getCategory().ordinal()]));
					results.put(ticket,true);
					}
				}
				
			});
			
		}
		return results;
		
	}
}
