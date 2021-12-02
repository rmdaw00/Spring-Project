package com.rmdaw.module15.web.controllers;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rmdaw.module15.aspects.Loggable;
import com.rmdaw.module15.business.facade.BookingFacadeImplementation;
import com.rmdaw.module15.data.model.classes.Event;
import com.rmdaw.module15.data.model.classes.local.EventLocal;
import com.rmdaw.module15.data.model.interfaces.IEvent;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/events")
public class EventController implements ControllerExtras {
	
	private BookingFacadeImplementation facade;
	
	private static final String MESSAGE_SUCCESS = "msgSuccess";
	private static final String MESSAGE_ERROR = "msgError";
	private static final String MESSAGE_FORM_ERROR = "Error in the form";
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "date", new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}
	
	
	@ModelAttribute("controller") 
	public String getControllerName() {
		return "/events";
	}
	
	@ModelAttribute("placeholder")
	public String getPlaceHolder() {
		return "By Title";
	}
	
	public EventController(BookingFacadeImplementation facade) {
		super();
		this.facade = facade;
	}
	
	@Loggable
	@GetMapping
	public String getAllEvents(@RequestParam(required = false) String search, 
				@RequestParam(required = false, defaultValue = "1") int page, 
				@RequestParam(required = false, defaultValue = "10") Integer pageSize,
				Model model) {
		
		/*//ToDo - Session Based pageSize
		 * 
			//Accessing USER chosen default page size saved in parent class
			if (pageSize!=null) {
				userSession.setPageSize(pageSize); 
			} else {
				pageSize = userSession.getPageSize();
			}
		
		 */

		
		List<IEvent> events = facade.getEventsByTitle(search, pageSize, page);
		
		String pageSizeParam = pageSize!=10?"&pageSize="+pageSize:""; 
		model.addAttribute("searchURL" , "/events/search=?" + search + pageSizeParam);
		model.addAttribute("page", page);
		model.addAttribute("search",(search!=null)?search:"");
		model.addAttribute("events",events);
		return "events/home";
	}
	
	
	
	
	
	
	
	
	
	
	@Loggable
	@GetMapping("/date")
	public String getAllEventsByDate(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
				@RequestParam(required = false, defaultValue = "1") int page, 
				@RequestParam(required = false, defaultValue = "10") Integer pageSize,
				Model model) {
		
		/*//ToDo - Session based pageSize
		 * 
			//Accessing USER chosen default page size saved in parent class
			if (pageSize!=null) {
				userSession.setPageSize(pageSize); 
			} else {
				pageSize = userSession.getPageSize();
			}
		
		 */
		
		List<IEvent> events;
		if (date != null)  {
			events = facade.getEventsForDay(date, pageSize, page);
		} else {
			date = new Date();
			events = facade.getEventsByTitle("", pageSize, page);
		}
		model.addAttribute("searchDate", date);
		model.addAttribute("events",events);
		return "events/date";
	}
	
	
	
	
	@Loggable
	@GetMapping("/create")
	public String createEvent(Model model) {
		model.addAttribute("action", "/events/create");
		return "events/form";
	}
	@Loggable
	@PostMapping("/create")
	public String createEvent(@Valid @ModelAttribute EventLocal event, 
							BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors() || event.getTitle().isBlank()) {
			model.addAttribute(MESSAGE_ERROR, MESSAGE_FORM_ERROR);
			return createEvent(model);
		}
		model.addAttribute(MESSAGE_SUCCESS, "Event Created Successfully");
		facade.createEvent(event);	
		return getAllEvents(event.getTitle(), 1, 10, model);
	}
	
	
	
	
	
	
	
	@Loggable
	@GetMapping("/edit")
	public String editEvent(@RequestParam Long id, Model model) {
		IEvent event = null;
		if (id!=null) {
			event = facade.getEventById(id);
		} 
		
		if (event==null) {
			model.addAttribute(MESSAGE_ERROR, "Event was not Found");
			return createEvent(model);
		}
		
		
		model.addAttribute("action", "/events/edit");
		model.addAttribute("event",event);
		
		return "events/form";
	}
	
	@Loggable
	@PostMapping("/edit")
	public String editEvent(@Valid @ModelAttribute EventLocal event, 
							BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute(MESSAGE_ERROR, MESSAGE_FORM_ERROR);
			return editEvent(event.getId(), model);	
		}
		
		facade.updateEvent(event);	
		model.addAttribute(MESSAGE_SUCCESS, "Event was Editted Successfully");
		
		return getAllEvents(event.getTitle(), 1, 10, model);
	}
	
	
	
	
	
	
	
		
	@Loggable
	@GetMapping("/delete")
	public String deleteEvent(@RequestParam Long id, Model model) {

		if (facade.deleteEvent(id)) {
			model.addAttribute(MESSAGE_SUCCESS, "Event Deleted Successfully");
			return getAllEvents("", 1, 10, model);
		} 
		
		model.addAttribute(MESSAGE_ERROR, "Event was not Found");
		return getAllEvents("", 1, 10, model);
	}
		
		
	
	
	
	

	
}
