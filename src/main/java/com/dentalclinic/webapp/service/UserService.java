package com.dentalclinic.webapp.service;

import com.dentalclinic.webapp.model.User;
import com.dentalclinic.webapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    // Repository interface for user data persistence operations
    private final UserRepository userRepository;
    // Encoder used to securely hash passwords before database storage
    private final PasswordEncoder passwordEncoder;

    //Constructor-based dependency injection
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }
}
