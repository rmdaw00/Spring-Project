package com.rmdaw.module15.data.model.classes.collections.local;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.rmdaw.module15.data.model.classes.local.TicketLocal;
import com.rmdaw.module15.data.model.interfaces.ITicket;


/*
 * created for the purpose of marshaling only 
 */
@XmlRootElement(name = "tickets")
@XmlAccessorType (XmlAccessType.FIELD)
public class Tickets {
	
	@XmlElement(name = "ticket")
	private List<TicketLocal> tickets = null;
	
	public Tickets(List<TicketLocal> tickets) {
		super();
		this.tickets = tickets;
	}

	public Tickets() {
	}

	public List<TicketLocal> getTickets() {
		return tickets;
	}

	public void setTickets(List<TicketLocal> tickets) {
		this.tickets = tickets;
	}
	
	
}
