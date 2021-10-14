package com.rmdaw.module15.web;

import java.awt.Color;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.rmdaw.module15.aspects.Loggable;
import com.rmdaw.module15.business.facade.BookingFacadeImplementation;
import com.rmdaw.module15.data.model.interfaces.IEvent;
import com.rmdaw.module15.data.model.interfaces.ITicket;
import com.rmdaw.module15.data.model.interfaces.IUser;

@Service
public class TicketPDFReportGenerator {
	
	
	private BookingFacadeImplementation facade;
	
	
	
	public TicketPDFReportGenerator(BookingFacadeImplementation facade) {
		super();
		this.facade = facade;
	}



	@Loggable
	public void export(HttpServletResponse response) {
		try (Document pdfDocument = new Document(PageSize.A4)){
			PdfWriter.getInstance(pdfDocument, response.getOutputStream());
			
			pdfDocument.open();
			
			//HEADER AND TITLE
			Font fontTitles = FontFactory.getFont(FontFactory.TIMES_BOLD);
	        fontTitles.setSize(16);

	        Paragraph title = new Paragraph();
	        title.setFont(fontTitles);
	        title.setAlignment(Element.ALIGN_CENTER);

	        title.add("Booked Tickets Report");
	        title.add(Chunk.NEWLINE);
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	        title.add("Date: " + formatter.format(new Date()));
	        
	        pdfDocument.add(title);
	        
	        
	        //TABLE 
	        PdfPTable table = new PdfPTable(new float[] {1F,4F,3F,1F});
	        table.setWidthPercentage(100.0f);
	        table.setSpacingBefore(10);
	         
	        // TABLE HEADER
	        Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD);
	        fontHeader.setColor(Color.WHITE);
	         
	        PdfPCell cell = new PdfPCell();
	        cell.setBackgroundColor(Color.GRAY);
	        cell.setPadding(5);
	        
	        for (String th: new String[]{"id", "Event", "User", "Place"})  {
	        	cell.setPhrase(new Phrase(th,fontHeader));
		        table.addCell(cell);
	        }
	         
	         
	        // TABLE DATA CELLS
	        	//Importing Data 
	        List<ITicket> tickets= new ArrayList<>();
			for (IUser user:facade.getUsersByName("", 1000, 1)) { //XXX: not scalable
				tickets.addAll(facade.getBookedTickets(user, 1000, 1));
			}
			IUser user;
			IEvent event;
	        for (ITicket ticket : tickets) {
	        	
	            table.addCell(String.valueOf(ticket.getId()));
	            
	            event = facade.getEventById(ticket.getEventId());
	            table.addCell(event==null ? "Deleted (id="+ticket.getEventId() 
	            						: event.getTitle());
	            
	            user = facade.getUserById(ticket.getUserId());
	            table.addCell(user==null ? "Deleted (id="+ticket.getUserId() 
	            						: user.getName());
	            
	            table.addCell(String.valueOf(ticket.getPlace()));
	        }
	         
	        pdfDocument.add(table);
			
		} catch (DocumentException | IOException e) {
			//XXX implement Logging
		} 
		
		
	}
}
