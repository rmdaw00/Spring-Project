package com.rmdaw.module15.data.model.interfaces;

import java.util.Date;



/**
 * Created by maksym_govorischev.
 */

public interface IEvent {
    /**
     * Event id. UNIQUE.
     * @return Event Id
     */
	
	
    long getId();
    void setId(long id);
    String getTitle();
    void setTitle(String title);
    Date getDate();
    void setDate(Date date);
}
