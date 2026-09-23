package com.dentalclinic.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Securityconfig {
    @Bean
    public PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder(); // If i use it as a bean i can access to bcrypt from anywhere
    }
}
