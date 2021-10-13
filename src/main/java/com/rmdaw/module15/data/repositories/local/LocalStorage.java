package com.rmdaw.module15.data.repositories.local;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Object controlled by spring, that gives access to the local storage map
 * use of this class(versus DB) is controlled by property in application.properties
 * 
 * @author Rashed_Dawabsheh
 *
 */
@Component
public class LocalStorage {
	
	private final ObjectMapper mapper;
	private LocalMap database = new LocalMap();
	
	@Value("${app.localStore}")
	private String jsonPath;
	/**
	 * .activateDefaultTyping(...)
	 * explicitly embeds object type inside json,
     * i.e. allows casting back from Object -> Event 
     * 
	 * @param objectMapper from Jackson
	 */
	@Autowired
	public LocalStorage(ObjectMapper objectMapper) {
		mapper = objectMapper;
		mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), 
									ObjectMapper.DefaultTyping.NON_FINAL);
	}
	
	
	
	/**
	 * Loads data into local map from local json
	 * @throws IOException 
	 */
	@PostConstruct //XXX
	public void loadData() throws IOException {
		System.out.println(jsonPath);
		try {
			database = mapper.readValue(new File(jsonPath),
								LocalMap.class);
			database.setIdCount((int)database.get("idCount"));
		} catch (IOException e) {
			System.err.println("failed to load json"); 
			//throw e;
			//FIXME into Logger
		} 
	}
	
	/**
	 * saves data into json from local map
	 */
	public void saveData() {
		 //XXX: using Spring Resource and writing back to it
		 //XXX: usage of property
		
		try (FileWriter fileWriter = new FileWriter(jsonPath)){
			database.put("idCount", database.getIdCount());
			String jsonResult = mapper.writerWithDefaultPrettyPrinter()
					  .writeValueAsString(database);
			
			fileWriter.write(jsonResult);
			
		} catch (IOException e) {
			System.err.println("failed to save json"); //FIXME into Logger
			e.printStackTrace();
		}
	}
	
	public LocalMap getDatabase() {
		return database;
	}

	public void setDatabase(LocalMap database) {
		this.database = database;
		
	}
}
