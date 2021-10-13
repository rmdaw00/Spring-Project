package com.rmdaw.module15.data.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rmdaw.module15.data.model.classes.Event;

@Repository
public interface EventsRepository extends JpaRepository<Event, Long> {
	
	/*
	 * Found an easier way han @Query using __Containing
	 * @Query ("SELECT e FROM events e WHERE e.eventtitle LIKE %:title%")
	 */
	public List<Event> findEventByEventTitleContaining(String title, Pageable pageable);
	
	public Page<Event> findAll(Pageable pageable);
	
	public List<Event> findEventByEventDate(Date date, Pageable pageable);

}
