package com.rmdaw.module15.data.model.interfaces;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.rmdaw.module15.data.model.classes.local.TicketLocal;

/**
 * Created by maksym_govorischev.
 */

/*
 * 
 * 
 * For review purposes only:
 * 
 * Did not work to make Jaxb accept Interfaces, 
 * Naturally Jaxb dont work with interfaces, ?module task?
 * 
 * but according to a comment on a blog I gave this a try
 * I tried the following too
	@XmlRootElement(name = "ticket")
	@XmlAccessorType (XmlAccessType.FIELD)
	@XmlType(factoryClass=TicketLocal.class, factoryMethod = "createTicket")
 * 
 * 
 */

public interface ITicket {
	
    public enum Category {STANDARD, PREMIUM, BAR}

    /**
     * Ticket Id. UNIQUE.
     * @return Ticket Id.
     */
    long getId();
    void setId(long id);
    long getEventId();
    void setEventId(long eventId);
    long getUserId();
    void setUserId(long userId);
    Category getCategory();
    void setCategory(Category category);
    int getPlace();
    void setPlace(int place);
    
    
    }
