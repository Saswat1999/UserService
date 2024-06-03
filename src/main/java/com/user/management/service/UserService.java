package com.user.management.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.management.dao.GenericResponse;
import com.user.management.dao.Status;
import com.user.management.entity.User;
import com.user.management.repository.UserRepository;

@Service
public class UserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	UserEventPublisher userEventPublish;

	public GenericResponse registerUser(User user) {
		Optional<User> findByUsername = userRepository.findByUsername(user.getUsername());
		if(findByUsername.isPresent()) {
			return new GenericResponse(400, "Username "+ user.getUsername()+ " already exist!", Status.FAILED);
		}
		user.setRole(user.getRole().toUpperCase());
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		User savedUser = userRepository.save(user);
		if(savedUser!= null) {
			return new GenericResponse(200, "Username : "+user.getUsername()+" Registered Successfully", Status.SUCCESS);
		}
		else
			return new GenericResponse(400, "Username "+ user.getUsername()+ " failed to register!", Status.FAILED);
	}
	
	public List<User> getUserList(){
		return userRepository.findAll();
	}

	public Object getUserByUsername(String username) {
		Optional<User> findByUsername = userRepository.findByUsername(username);
		if(findByUsername.isEmpty()) {
			return new GenericResponse(400, "Username "+ username+ " Not Found !", Status.FAILED);
		}
		return findByUsername;
	}

	public GenericResponse updateUser(User user) {
		Optional<User> findByUsername = userRepository.findByUsername(user.getUsername());
		if(findByUsername.isEmpty()) {
			return new GenericResponse(400, "Username "+ user.getUsername()+ " Not Found! ", Status.FAILED);
		}
		user.setRole(user.getRole().toUpperCase());
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		userRepository.save(user);
		return new GenericResponse(200, "Username : "+user.getUsername()+" Updated Successfully", Status.SUCCESS);
	}

	public void deleteUserByUsername(String username) {
		userRepository.deleteByUsername(username);
	}

	public boolean userExists(Long id) {
		return userRepository.findById(id).isPresent();
	}
	

}
