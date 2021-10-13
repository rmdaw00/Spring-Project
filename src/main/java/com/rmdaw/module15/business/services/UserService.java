package com.rmdaw.module15.business.services;


import java.util.List;

import org.springframework.stereotype.Service;

import com.rmdaw.module15.data.DAOs.UserDAO;
import com.rmdaw.module15.data.model.interfaces.IUser;

@Service
public class UserService {

	private final UserDAO userDAO;
	
	public UserService(UserDAO userDAO) {
		this.userDAO = userDAO;	
	}
	
	public IUser getUserById(long userId) {
		return userDAO.getUserById(userId);
	}

	
	public IUser getUserByEmail(String email) {
		return userDAO.getUserByEmail(email);
	}

	
	public List<IUser> getUsersByName(String name, int pageSize, int pageNum) {
		return userDAO.getUsersByName(name, pageSize, pageNum);
	}
	

	
	public IUser createUser(IUser user) {
		return userDAO.createUser(user);
	}


	public IUser updateUser(IUser user) {
		return userDAO.updateUser(user);
	}


	public boolean deleteUser(long userId) {
		return userDAO.deleteUser(userId);
	}
	
	
}
