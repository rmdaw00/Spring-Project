package com.rmdaw.module15.data.DAOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.rmdaw.module15.aspects.UpdateLocalDB;
import com.rmdaw.module15.data.model.classes.User;
import com.rmdaw.module15.data.model.classes.local.UserLocal;
import com.rmdaw.module15.data.model.interfaces.IUser;
import com.rmdaw.module15.data.repositories.UsersRepository;
import com.rmdaw.module15.data.repositories.local.LocalStorage;
/**
 * These come from parent
 * 		Boolean localDataSet
 * 		LocalMap localDB
 * 
 * @author Rashed_Dawabsheh
 *
 */
@Component
public class UserDAO extends CommonDAO {

	private UsersRepository userRepo;
	
	
	@Autowired
	public UserDAO(UsersRepository userRepo, LocalStorage localStorage) {
		super(localStorage);
		this.userRepo = userRepo;
	}
	
	
	public IUser getUserById(long userId) {
		if (!localDataSet) {
			return userRepo.findById(userId).orElse(null);
		} else {
			return localDB.getUser(userId);
		}
	}

	
	public User getUserByEmail(String email) {
		if (!localDataSet) {		
			return userRepo.findUserByUserEmail(email).orElse(null);
		} else {
			return (User) localDB.getAllUsers().stream()
						.filter(user -> user.getEmail().equalsIgnoreCase(email))
						.limit(1); //should be one anyway
		}
	}	

	
	public List<IUser> getUsersByName(String name, int pageSize, int pageNum) {
		List<IUser> users= new ArrayList<>();	
		int currentPage = pageNum-PAGING_OFFSET;
		if (!localDataSet) {		
			//DATABASE
			Pageable pageable = PageRequest.of(currentPage, pageSize);
			users.addAll(userRepo.findUserByUserNameContaining(name, pageable));
			return users;
			
		} else {
			//LOCAL STORAGE
			List<UserLocal> usersResult = localDB.getAllUsers();
			return usersResult.stream()
						.filter(user -> {
							if (name==null) return true;
							return user.getName().toLowerCase().contains(name.toLowerCase());
						})
						.sorted()
						.skip((long)pageSize*currentPage)
						.limit(pageSize)
						.collect(Collectors.toList());
		}
		
	}

	@UpdateLocalDB
	public IUser createUser(IUser user) {
		if (!localDataSet) {
			return userRepo.save((User)user);
		} else {
			localDB.putUser((UserLocal)user);
			return user;
		}
	}

	@UpdateLocalDB
	public IUser updateUser(IUser user) {
		if (!localDataSet) {
			Optional<User> eventOpt = userRepo.findById(user.getId());
			if (eventOpt.isPresent()) {
				return userRepo.save((User)user);
			} else {
				return null;
			}
		} else {
			if (localDB.getUser(user.getId())!=null) {
				return localDB.putUser((UserLocal)user);
			} else {
				return null;
			}
		}
	}

	@UpdateLocalDB
	public boolean deleteUser(long userId) {
		if (!localDataSet) {
			Optional<User> userOpt = userRepo.findById(userId);
			if (userOpt.isPresent()) {
				User user = userOpt.get();
				userRepo.delete(user);
				return true;
			} else {
				return false;
			}
			
		} else {
			if (localDB.getUser(userId)!=null) {
				return localDB.removeUser(userId);
			} else {
				return false;
			}
		}
	}
	
	
	
}
