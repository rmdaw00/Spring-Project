package com.rmdaw.module15.data.DAOs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.rmdaw.module15.aspects.UpdateLocalDB;
import com.rmdaw.module15.data.model.classes.Event;
import com.rmdaw.module15.data.model.classes.local.EventLocal;
import com.rmdaw.module15.data.model.interfaces.IEvent;
import com.rmdaw.module15.data.repositories.EventsRepository;
import com.rmdaw.module15.data.repositories.local.LocalStorage;

/**
 * localDataSet comes from parent
 * 
 * @author Rashed_Dawabsheh
 *
 */
@Component
public class EventDAO extends CommonDAO {

	private EventsRepository eventRepo;
	
	@Autowired
	public EventDAO(EventsRepository eventRepo, LocalStorage localStorage) {
		super(localStorage);
		this.eventRepo = eventRepo;
	}
	
	public IEvent getEventById(long eventId) {
		if (!localDataSet) {
			return eventRepo.findById(eventId).orElse(null);
		} else {
			return localDB.getEvent(eventId);
		}
	}
	 
	public List<IEvent> getEventsByTitle(String title, int pageSize, int pageNum) {
		int currentPage = pageNum-PAGING_OFFSET;
		
		if (!localDataSet) {
			// DATABASE
			Pageable pageable = PageRequest.of(currentPage, pageSize);
			
			if (title != null) {
				List<IEvent> events = new ArrayList<>();
				eventRepo.findEventByEventTitleContaining(title,pageable).forEach(events::add);
				return events;				
			} else {
				return eventRepo.findAll(pageable).stream()
												.sorted()
												.skip((long)pageSize*currentPage)
												.limit(pageSize)
												.collect(Collectors.toList());
			}
		
		} else {
			// LOCAL STORAGE
			if (title == null) {
				//GET ALL
				return localDB.getAllEvents().stream()
											.sorted()
											.skip((long)pageSize*currentPage)
											.limit(pageSize)
											.collect(Collectors.toList());
			} else {
				//GET BY TITLE NAME
				return localDB.getAllEvents()
							.stream()
							.filter(event -> event.getTitle().toLowerCase()
												.contains(title.toLowerCase()))
							 .sorted()
							 .skip((long)pageSize*currentPage)
							 .limit(pageSize)
							 .collect(Collectors.toList());
			}
		}
	}
	
	public List<IEvent> getEventsForDay(Date day, int pageSize, int pageNum){
		int currentPage = pageNum-PAGING_OFFSET;
		if (!localDataSet) {
			//DATABASE
			Pageable pageable = PageRequest.of(currentPage, pageSize);
			Iterable<Event> iterableEvents = eventRepo.findEventByEventDate(day,pageable);
			
			return StreamSupport.stream(iterableEvents.spliterator(),false)
								.collect(Collectors.toList());
		} else {
			//LOCAL STORAGE
			return localDB.getAllEvents()
						.stream().filter(event -> event.getDate().equals(day))
								.sorted()
								.skip((long)pageSize*currentPage)
								.limit(pageSize)
								.collect(Collectors.toList());
		}
	}
	
	@UpdateLocalDB
	public IEvent createEvent(IEvent event) {
		if (!localDataSet) {
			return eventRepo.save((Event)event);
		} else {
			localDB.putEvent((EventLocal)event);
			return event;
		}
	}

	@UpdateLocalDB
	public IEvent updateEvent(IEvent event) {
		if (!localDataSet) {
			Optional<Event> eventOpt = eventRepo.findById(event.getId());
			if (eventOpt.isPresent()) {
				return eventRepo.save((Event)event);
			} else {
				return null;
			}
		} else {
			if (localDB.getEvent(event.getId())!=null) {
				return localDB.putEvent((EventLocal)event);
			} else {
				return null;
			}
		}
	}
	
	@UpdateLocalDB
	public boolean deleteEvent(long eventId) {
		if (!localDataSet) {
			Optional<Event> eventOpt = eventRepo.findById(eventId);
			if (eventOpt.isPresent()) {
				Event event = eventOpt.get();
				eventRepo.delete(event);
				return true;
			} else {
				return false;
			}
			
		} else {
			if (localDB.getEvent(eventId)!=null) {
				return localDB.removeEvent(eventId);
			} else {
				return false;
			}
		}
		
	}

	
	
}
