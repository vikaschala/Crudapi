package com.nit.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nit.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	 Optional<User> findByUsername(String username);
	Optional<User> findByEmailId(String emailId);
	 
}
