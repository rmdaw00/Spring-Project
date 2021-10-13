package com.rmdaw.module15.data.model.classes;

import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.rmdaw.module15.data.model.interfaces.IUser;

/**
 * Created by maksym_govorischev on 14/03/14.
 */
@Entity
@Table(name = "users")
public class User implements IUser, Comparable<User> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="userid")
	private long userId = 0;
	
	
	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
	private Set<Ticket> tickets;
	
	
	@Column(name="username")
	private String userName;
	
	@Column(name="useremail")
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
	public int compareTo(User o) {
		return getEmail().compareTo(o.getEmail());
	}

	@Override
	public int hashCode() {
		return Objects.hash(tickets, userEmail, userId, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(tickets, other.tickets) && Objects.equals(userEmail, other.userEmail)
				&& userId == other.userId && Objects.equals(userName, other.userName);
	}

	
}
