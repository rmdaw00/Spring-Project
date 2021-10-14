package com.rmdaw.module15.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import com.rmdaw.module15.business.facade.BookingFacadeImplementation;
import com.rmdaw.module15.data.model.classes.collections.local.Tickets;
import com.rmdaw.module15.data.model.classes.local.EventLocal;
import com.rmdaw.module15.data.model.classes.local.TicketLocal;
import com.rmdaw.module15.data.model.classes.local.UserLocal;
import com.rmdaw.module15.data.model.interfaces.ITicket;
import com.rmdaw.module15.data.model.interfaces.IUser;

@Service
public class TicketMarshler {
	
	private Jaxb2Marshaller marshaller;
	private BookingFacadeImplementation facade;

	@Value("${app.localXMLStore}")
	private String xmlPath;
	
	public TicketMarshler(Jaxb2Marshaller marshaller, BookingFacadeImplementation facade) {
		super();
		this.marshaller = marshaller;
		this.facade = facade;
	}
	
	/*
	 * 
	 * Limitation was here is that Jaxb cannot work with interfaces
	 * thus I had to revert back to abstract classes
	 * 
	 * 
	 */
	public String ticketsToXML()  {
     
		Tickets tickets= new Tickets();
		
		List<TicketLocal> ticketIntList= new ArrayList<>();
		for (IUser user:facade.getUsersByName("", 1000, 1)) { //XXX: not scalable
			
			facade.getBookedTickets(user, 1000, 1).stream()
			.map(TicketLocal.class::cast)
			.forEach(ticketIntList::add);
		}
		
		tickets.setTickets(ticketIntList);
		
        try (FileOutputStream os = new FileOutputStream(xmlPath)) {    
            marshaller.marshal(tickets, new StreamResult(os));       
            return xmlPath;
        } catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
        
    }
	
	
	

	
	
}
