package com.rmdaw.module15.web.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rmdaw.module15.business.facade.BookingFacadeImplementation;
import com.rmdaw.module15.data.model.classes.local.TicketLocal;
import com.rmdaw.module15.data.model.interfaces.IEvent;
import com.rmdaw.module15.data.model.interfaces.ITicket;
import com.rmdaw.module15.data.model.interfaces.IUser;
import com.rmdaw.module15.web.PDFTicketReportGenerator;

@Controller
@RequestMapping("/tickets")
public class TicketController {

	private BookingFacadeImplementation facade;
	private PDFTicketReportGenerator pdfReportGen;

	private static final String MESSAGE_SUCCESS = "msgSuccess";
	private static final String MESSAGE_ERROR = "msgError";
	private static final String MESSAGE_FORM_ERROR = "Error in the form";

	public TicketController(BookingFacadeImplementation facade,
							PDFTicketReportGenerator pdfReportGen) {
		super();
		this.facade = facade;
		this.pdfReportGen = pdfReportGen;
	}
	
	@GetMapping
	public String getTicketsByUser(@RequestParam(required = false) Long userId,
			@RequestParam(required = false, defaultValue = "1") int page, 
			@RequestParam(required = false, defaultValue = "10") Integer pageSize,
			Model model) {
		List<ITicket> tickets= new ArrayList<>();
		IUser user = null;
		if (userId!=null) {
			if ((user = facade.getUserById(userId))!=null) {
				tickets = facade.getBookedTickets(user, pageSize, page);
			}
		}
		
		model.addAttribute("userId", userId);
		model.addAttribute("tickets", tickets);
		return "tickets/home";
	}
	
	
	@GetMapping("/ReportPDF")
	public void getTicketsReport(HttpServletResponse response) {
		response.setContentType("application/pdf");
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_hh:mm");
		
		response.setHeader("Content-Disposition", "inline; "
			+ "filename=TicketReport" + formatter.format(new Date()) + ".pdf");
		
		pdfReportGen.export(response);
	}
	
	@GetMapping("/event")
	public String getTicketsByEvent(@RequestParam(required = false) Long eventId,
			@RequestParam(required = false, defaultValue = "1") int page, 
			@RequestParam(required = false, defaultValue = "10") Integer pageSize,
			Model model) {
		List<ITicket> tickets= new ArrayList<>();
		IEvent event = null;
		if (eventId!=null) {
			if ((event = facade.getEventById(eventId))!=null) {
				tickets = facade.getBookedTickets(event, pageSize, page);
			}
		}
		
		model.addAttribute("eventId", eventId);
		model.addAttribute("tickets", tickets);
		return "tickets/event";
	}
	
	
	
	
	@GetMapping("/create")
	public String createTicket(Model model) {
		model.addAttribute("action", "/tickets/create");
		return "tickets/form";
	}
	
	@PostMapping("/create")
	public String createTicket(@Valid @ModelAttribute TicketLocal ticket, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute(MESSAGE_ERROR, MESSAGE_FORM_ERROR);
			return createTicket(model);
		}
		
		if (facade.bookTicket(ticket.getUserId(), ticket.getEventId(), ticket.getPlace(), ticket.getCategory())==null) {
			model.addAttribute(MESSAGE_ERROR, "Place already Taken");
			return createTicket(model);
		} else {
			model.addAttribute(MESSAGE_SUCCESS, "Ticket Booked Successfully");
			return "tickets/form";
		}
	}
	
	@GetMapping("/cancel")
	public String createTicket(@RequestParam Long id, Model model) {
		if (null!=id && facade.cancelTicket(id)) {
			model.addAttribute(MESSAGE_ERROR, "Ticket was Deleted Successfully");
			return getTicketsByUser(Long.valueOf(0),10,1,model);
		}
		
		model.addAttribute(MESSAGE_ERROR, "Ticket was not Found");
		return getTicketsByUser(Long.valueOf(0),10,1,model);
	
	}
	
}

