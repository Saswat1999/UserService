package com.user.management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.management.entity.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
	
    Optional<User> findByUsername(String username);

    @Transactional
	void deleteByUsername(String username);
}
