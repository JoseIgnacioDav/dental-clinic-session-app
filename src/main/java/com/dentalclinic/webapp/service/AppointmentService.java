package com.dentalclinic.webapp.service;

import com.dentalclinic.webapp.repository.AppointmentRepository;
import com.dentalclinic.webapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


}
