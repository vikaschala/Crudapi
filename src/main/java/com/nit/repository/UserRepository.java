package com.nit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nit.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
