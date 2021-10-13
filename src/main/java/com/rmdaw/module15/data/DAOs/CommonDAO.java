package com.rmdaw.module15.data.DAOs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rmdaw.module15.aspects.Loggable;
import com.rmdaw.module15.data.repositories.local.LocalMap;
import com.rmdaw.module15.data.repositories.local.LocalStorage;

@Service
public abstract class CommonDAO {
	@Value("${app.localDataSet}")
	protected boolean localDataSet;
	
	protected LocalMap localDB;
	protected LocalStorage localStorage;
	protected static final int PAGING_OFFSET = 1;
	
	@Autowired
	protected CommonDAO(LocalStorage localStorage) {
		this.localStorage =localStorage;
		localDB = localStorage.getDatabase();
	}
	
	@Loggable
	public void saveLocalDB() {
		if (localDataSet) {
			localStorage.saveData();

			System.out.println("called from common class");
		}
	}
}
