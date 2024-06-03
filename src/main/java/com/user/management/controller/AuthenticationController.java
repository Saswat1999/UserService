package com.user.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.management.config.JwtUtil;
import com.user.management.dao.AuthenticationRequest;
import com.user.management.dao.AuthenticationResponse;
import com.user.management.dao.GenericResponse;
import com.user.management.dao.Status;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication Controller", description = "Authenticator Management API")
@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "generate jwt token", description = "generate jwt for user to use the services")
    @ApiResponse(responseCode = "200", description = "Token generated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class)))
    @ApiResponse(responseCode = "400", description = "User authentication failed. Wrong username or password!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class)))
    @PostMapping("/generatetoken")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse(400, "Incorrect username or password", Status.FAILED));
        } catch (UsernameNotFoundException e) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse(400, "Incorrect username or password", Status.FAILED));
		}

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
