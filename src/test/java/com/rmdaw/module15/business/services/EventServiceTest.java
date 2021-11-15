package com.rmdaw.module15.business.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rmdaw.module15.data.DAOs.EventDAO;
import com.rmdaw.module15.data.model.interfaces.IEvent;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

	@Nested
	class getEventsByTitle {
		@Mock 
		EventDAO eventDAO;
		
		@InjectMocks
		EventService undertest;
		
		@Captor
		ArgumentCaptor<String> titleCaptor;
		@Captor
		ArgumentCaptor<Integer> pageSizeCaptor;
		@Captor
		ArgumentCaptor<Integer> pageNumCaptor;
		
		@ParameterizedTest
		@CsvSource({"test,5,1", ",10,2"})
		@DisplayName("Testing Service call")
		void getEventsByTitleA(String title, int pageSize,int pageNum  ) {
			
			when(eventDAO.getEventsByTitle(title,pageSize,pageNum)).thenReturn(new ArrayList<IEvent>());
			
			undertest.getEventsByTitle(title, pageSize, pageNum);
			
			
			verify(eventDAO).getEventsByTitle(titleCaptor.capture(),
											pageSizeCaptor.capture(),
											pageNumCaptor.capture());
			
			assertEquals(title, titleCaptor.getValue());
			assertEquals(pageSize, pageSizeCaptor.getValue());
			assertEquals(pageNum, pageNumCaptor.getValue());
		}
	}

}
