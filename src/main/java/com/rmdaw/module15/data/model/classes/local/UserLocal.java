package com.rmdaw.module15.data.model.classes.local;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Component;

import com.rmdaw.module15.data.model.interfaces.IUser;


@Component
public class UserLocal implements IUser, Comparable<UserLocal> {

	private long userId = 0;

	@NotEmpty
	private String userName;
	
	@NotEmpty
	private String userEmail;
	
	@Override
	public long getId() {
		return userId;
	}

	@Override
	public void setId(long id) {
		userId = id;
		
	}

	@Override
	public String getName() {
		return userName;
	}

	@Override
	public void setName(String name) {
		userName = name;
	}

	@Override
	public String getEmail() {
		return userEmail;
	}

	@Override
	public void setEmail(String email) {
		userEmail = email;
	}

	@Override
	public int compareTo(UserLocal o) {
		return getEmail().compareTo(o.getEmail());
	}

	@Override
	public int hashCode() {
		return Objects.hash(userEmail, userId, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserLocal other = (UserLocal) obj;
		return Objects.equals(userEmail, other.userEmail) && userId == other.userId
				&& Objects.equals(userName, other.userName);
	}

	
	
}
