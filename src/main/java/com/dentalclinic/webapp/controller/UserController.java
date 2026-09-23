package com.dentalclinic.webapp.controller;

import com.dentalclinic.webapp.dto.request.auth.LoginRequestDTO;
import com.dentalclinic.webapp.dto.request.user.UserRequestDTO;
import com.dentalclinic.webapp.dto.response.user.UserResponseDTO;
import com.dentalclinic.webapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")

public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }
    //registration endpoint
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register (@RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO newUser = userService.userRegistration(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    //login endpoint
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequestDTO credentials, HttpSession session){
        UserResponseDTO loggedUser = userService.login(credentials);
        session.setAttribute("loggedUser", loggedUser);
        return ResponseEntity.ok(loggedUser);
    }




}
