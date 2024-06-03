package com.user.management;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.user.management.controller.UserController;
import com.user.management.dao.GenericResponse;
import com.user.management.dao.Status;
import com.user.management.entity.User;
import com.user.management.service.UserService;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterUser_Success() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setRole("USER");

        GenericResponse response = new GenericResponse(200, "Username : testUser Registered Successfully", Status.SUCCESS);

        when(userService.registerUser(user)).thenReturn(response);

        ResponseEntity<?> result = userController.registerUser(user);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testRegisterUser_Failure() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setRole("USER");

        GenericResponse response = new GenericResponse(400, "Username testUser already exist!", Status.FAILED);

        when(userService.registerUser(user)).thenReturn(response);

        ResponseEntity<?> result = userController.registerUser(user);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }
    
    @Test
    void testGetAllUser() throws Exception {
        User user1 = new User(1L, "user1", "password1", "user");
        User user2 = new User(2L, "user2", "password2", "admin");

        when(userService.getUserList()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users/v1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].username").value("user1"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].username").value("user2"));

        verify(userService, times(1)).getUserList();
    }

    @Test
    void testGetUserDetailsbyUserName_UserFound() throws Exception {
        User user = new User(1L, "user1", "password1", "user");

        when(userService.getUserByUsername("user1")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/v1/userdetails").param("username", "user1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("user1"));

        verify(userService, times(1)).getUserByUsername("user1");
    }

    @Test
    void testGetUserDetailsbyUserName_UserNotFound() throws Exception {
        String username = "nonexistentUser";
        GenericResponse response = new GenericResponse(400, "Username " + username + " Not Found !", Status.FAILED);

        when(userService.getUserByUsername(username)).thenReturn(response);

        mockMvc.perform(get("/users/v1/userdetails").param("username", username))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Username " + username + " Not Found !"))
            .andExpect(jsonPath("$.status").value("FAILED"));

        verify(userService, times(1)).getUserByUsername(username);
    }
    
    @Test
    void testUpdateUser_Success() throws Exception {
        User user = new User(1L, "user1", "password1", "USER");
        GenericResponse response = new GenericResponse(200, "Username : user1 Updated Successfully", Status.SUCCESS);

        when(userService.updateUser(any(User.class))).thenReturn(response);

        mockMvc.perform(put("/users/v1/updateuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"username\":\"user1\",\"password\":\"password1\",\"role\":\"USER\"}"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Username : user1 Updated Successfully"))
            .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(userService, times(1)).updateUser(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() throws Exception {
        String username = "nonexistentUser";
        GenericResponse response = new GenericResponse(400, "Username " + username + " Not Found! ", Status.FAILED);

        when(userService.updateUser(any(User.class))).thenReturn(response);

        mockMvc.perform(put("/users/v1/updateuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"username\":\"nonexistentUser\",\"password\":\"password1\",\"role\":\"USER\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Username nonexistentUser Not Found! "))
            .andExpect(jsonPath("$.status").value("FAILED"));

        verify(userService, times(1)).updateUser(any(User.class));
    }
    
    @Test
    void testDeleteUserDetailsbyUserName_Success() throws Exception {
        String username = "user1";

        doNothing().when(userService).deleteUserByUsername(username);

        mockMvc.perform(delete("/users/v1/deleteuser")
                .param("username", username))
            .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUserByUsername(username);
    }
}
