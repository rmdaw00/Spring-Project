package com.rmdaw.module15.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rmdaw.module15.aspects.Loggable;
import com.rmdaw.module15.business.facade.BookingFacadeImplementation;
import com.rmdaw.module15.data.model.classes.User;
import com.rmdaw.module15.data.model.classes.local.UserLocal;
import com.rmdaw.module15.data.model.interfaces.IUser;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {
	
	private BookingFacadeImplementation facade;
	private static final String USER_NOT_FOUND = "User not found";
	
	@Value("${app.localDataSet}")
	private Boolean local;
	
	@Autowired
	public UsersRestController(BookingFacadeImplementation facade) {
		this.facade = facade;
	}
	
	@Loggable
	@GetMapping(params = "userId",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IUser> getUserByID(@RequestParam(value="userId") long userId) {
		IUser user = facade.getUserById(userId);
		
		if (user!=null) {
			return new ResponseEntity<>(user,HttpStatus.OK);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND);
		} 
	}
	
	@Loggable
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<IUser>> getUsersByName(
			@RequestParam(value="userName",required=false) String userName, 
			@RequestParam(value="pageSize", required = false, defaultValue = "20") int pageSize, 
			@RequestParam(value="page", required = false, defaultValue="1") int page) {
		List<IUser> users=facade.getUsersByName(userName, pageSize, page);
		
		if (!users.isEmpty()) {
			return new ResponseEntity<>(users,HttpStatus.FOUND);
		} else {
			return new ResponseEntity<>(users,HttpStatus.NOT_FOUND);
		}
		
	}
	
	
	@Loggable
	@PostMapping (params = {"userName","userEmail"},produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IUser> createUser(
			@RequestParam(value="userName") String userName, 
			@RequestParam(value="userEmail") String userEmail) {
		IUser user=Boolean.TRUE.equals(local)?new UserLocal():new User();
		user.setName(userName);
		user.setEmail(userEmail);
		
		IUser result=facade.createUser(user);
		
		return new ResponseEntity<>(result,HttpStatus.CREATED);
	}
	
	@Loggable
	@PostMapping(params = {"userId","userName","userEmail"},
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IUser> updateUser(
			@RequestParam(value="userId") long userId,
			@RequestParam(value="userName") String userName, 
			@RequestParam(value="userEmail") String userEmail) {
		//Creation of user object
		IUser user = Boolean.TRUE.equals(local)?new UserLocal(): new User();
		user.setId(userId);
		user.setEmail(userEmail);
		user.setName(userName);
		
		//Calling Facade Method
		IUser result = facade.updateUser(user);
		if (result != null) {
			return new ResponseEntity<>(result,HttpStatus.FOUND);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND);
		}	
	}

	@Loggable
	@DeleteMapping (produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteUserByID(@RequestParam(value="userId") long userId) {
			if (facade.deleteUser(userId)) {
				return new ResponseEntity<>("Deleted",HttpStatus.OK);
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND);
			}
	}

}
