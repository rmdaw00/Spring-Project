package com.rmdaw.module15.data.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rmdaw.module15.data.model.classes.Ticket;

@Repository
public interface TicketsRepository extends JpaRepository<Ticket, Long> {
	//public List<Ticket> findTicketByTicketCategory(Category category, Pageable pageable);
	public Page<Ticket> findAll(Pageable pageable);
	
	//@Query("SELECT t FROM tickets INNER JOIN Events e ON tickets.eventid = e.eventid WHERE tickets.eventId = :eventId AND t.ticketplace = :place")
	//public List<Ticket> findEventPlaceisTaken(long eventId, int place);
	
	
//	@Query("SELECT t FROM public.tickets t "
//			+ " INNER JOIN public.events e ON t.eventid = e.eventid"
//			+ " INNER JOIN public.users u ON t.userid = u.userid"
//			+ " WHERE t.userid = :userId"
//			+ " ORDER BY e.eventdate ASC ")
	public List<Ticket> findTicketsByUserID(long userId, Pageable pageable);
	
//	@Query("SELECT t FROM tickets t "
//			+ " INNER JOIN public.events e ON t.eventid = e.eventid"
//			+ " INNER JOIN public.users u ON t.userid = u.userid"
//			+ " WHERE t.eventId = :eventId"
//			+ " ORDER BY u.useremail ASC ")
	public List<Ticket> findTicketsByEventID(long eventId, Pageable pageable);
	
	//public List<Ticket> findTicketsByEvent(Event event, Sort sort, Pageable pageable);
	//public List<Ticket> findTicketsByUser(User user, Sort sort, Pageable pageable);
}
