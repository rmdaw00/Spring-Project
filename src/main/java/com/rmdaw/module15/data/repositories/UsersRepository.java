package com.rmdaw.module15.data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rmdaw.module15.data.model.classes.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long>{

	public Optional<User> findUserByUserEmail(String userEmail);
	
	public List<User> findUserByUserNameContaining(String username, Pageable pageable);
	
}
