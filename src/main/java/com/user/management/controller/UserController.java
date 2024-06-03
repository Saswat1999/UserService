package com.user.management.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.management.dao.GenericResponse;
import com.user.management.dao.Status;
import com.user.management.dao.UserEvent;
import com.user.management.entity.User;
import com.user.management.service.UserEventPublisher;
import com.user.management.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Controller", description = "User Management API")
@RestController
@RequestMapping("/users/v1")
public class UserController {
	
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserEventPublisher userEventPublisher;

    @Operation(summary = "Register a new user", description = "Creates a new user in the system")
    @ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid user details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class)))
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
    	GenericResponse registerUser = userService.registerUser(user);
    	
    	return new ResponseEntity<>(registerUser, HttpStatusCode.valueOf(registerUser.getStatusCode()));
    }
    
    @Operation(summary = "Get all users", description = "Retrieves a list of all users in the system.", security = @SecurityRequirement(name = "bearer"))
    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @GetMapping
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.getUserList());
    }
    
    @Operation(summary = "Get user details", description = "Retrieves the details of a user by username.")
    @ApiResponse(responseCode = "200", description = "User details retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "400", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class)))
    @GetMapping("/userdetails")
    public ResponseEntity<?> getUserDetailsbyUserName(@Param(value = "username") String username) {
    	Object userByUsername = userService.getUserByUsername(username);
    	if(userByUsername instanceof GenericResponse)
    		return new ResponseEntity<>(userByUsername, HttpStatusCode.valueOf(((GenericResponse) userByUsername).getStatusCode()));
 
    	return ResponseEntity.ok(userByUsername);
    }
    
    @Operation(summary = "Update user", description = "Updates an existing user's details in the system.")
    @ApiResponse(responseCode = "200", description = "User update successful. Returns the updated user details.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class)))
    @ApiResponse(responseCode = "400", description = "User update failed due to invalid input or server error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class)))
    @PutMapping("/updateuser")
    public ResponseEntity<?> updateUser(@RequestBody User user){
    	GenericResponse registerUser = userService.updateUser(user);
    	
    	return new ResponseEntity<>(registerUser, HttpStatusCode.valueOf(registerUser.getStatusCode()));
    }
    
    @Operation(summary = "Delete user", description = "Deletes a user from the system by username.")
    @ApiResponse(responseCode = "204", description = "User deletion successful. No content returned.")
    @ApiResponse(responseCode = "404", description = "User deletion failed. User not found.")
    @DeleteMapping("/deleteuser")
    public ResponseEntity<?> deleteUserDetailsbyUserName(@Param(value = "username") String username) {
    	try {
            userService.deleteUserByUsername(username);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse(400, e.getMessage(), Status.FAILED));
        }
    }
    
    @Operation(summary = "Check if user exists", description = "Checks if a user exists in the system by user ID.")
    @ApiResponse(responseCode = "200", description = "Successfully checked the user existence. Returns true if exists, false otherwise.")
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> userExists(@PathVariable Long id) {
        boolean exists = userService.userExists(id);
        return ResponseEntity.ok(exists);
    }
    
    @Operation(summary = "Publish user event", description = "Publishes a user event to the system.")
    @ApiResponse(responseCode = "200", description = "User event published successfully.")
    @PostMapping("/event")
    public ResponseEntity<?> publishEvent(@RequestBody UserEvent userEvent) {
    	userEventPublisher.publishUserEvent(userEvent);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
}
