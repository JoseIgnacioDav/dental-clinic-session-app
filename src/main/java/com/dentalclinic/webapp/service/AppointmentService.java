package com.dentalclinic.webapp.service;

import com.dentalclinic.webapp.dto.request.appointment.AppointmentRequestDTO;
import com.dentalclinic.webapp.dto.response.appointment.AppointmentResponseDTO;
import com.dentalclinic.webapp.model.Appointment;
import com.dentalclinic.webapp.model.User;
import com.dentalclinic.webapp.repository.AppointmentRepository;
import com.dentalclinic.webapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;


    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    public AppointmentResponseDTO createappointmet (AppointmentRequestDTO appointment, Long patientIdSession){
        // use of http session to check if the user is logged in with a valid JSESSIONID
        Optional<User> loggedUserOPT = userRepository.findById(patientIdSession);
        if(loggedUserOPT.isEmpty()){
            throw new RuntimeException("Unable to find user");
        }
        User loggedUser = loggedUserOPT.get();
        Appointment safeAppointment = new Appointment();
        safeAppointment.setDate(appointment.getDate());
        safeAppointment.setTime(appointment.getTime());
        safeAppointment.setPatient(appointment.getPatient());
        safeAppointment.setDoctor(appointment.getDoctor());


        //who is creating the date:
        /**
         * If the role is PATIENT ignore the json and set the appointment for himself
         * if the role is DOCTOR or ADMIN third party appointment creation is allowed
         * **/
        if(loggedUser.getRole().equalsIgnoreCase("PATIENT")){
            safeAppointment.setPatient(loggedUser);
        } else if (loggedUser.getRole().equalsIgnoreCase("DOCTOR")||loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
            if (safeAppointment.getPatient() == null||safeAppointment.getPatient().getId() == null){
                throw new RuntimeException("You must specify the Patient's ID for this appointment");
            }
            //Patient validation
            Optional<User> jsonPatientOPT =userRepository.findById(safeAppointment.getPatient().getId());
            if(jsonPatientOPT.isEmpty()){
                throw new RuntimeException("The Patiend does not exist");
            }
            //turn it into User
            User jsonPatient = jsonPatientOPT.get();
            safeAppointment.setPatient(jsonPatient);
        }
        //Race condition Validation
        boolean occupied = appointmentRepository.existsByDoctorAndDateAndTime(
                safeAppointment.getDoctor(),
                safeAppointment.getDate(),
                safeAppointment.getTime()
        );

        if(occupied){
            throw new RuntimeException("We're sorry, this time slot has already been booked with the selected dentist.");
        }
        safeAppointment.setStatus("PENDING"); // force every appointment to be born as PENDING

        //save the appointment
        appointmentRepository.save(safeAppointment);

        //DTO return

        AppointmentResponseDTO safeAppointmentDTO = new AppointmentResponseDTO();
        safeAppointmentDTO.setId(safeAppointment.getId());
        safeAppointmentDTO.setStatus(safeAppointment.getStatus());
        safeAppointmentDTO.setTime(safeAppointment.getTime());
        safeAppointmentDTO.setDate(safeAppointment.getDate());
        safeAppointmentDTO.setPatientnames(safeAppointment.getPatient().getFirstnames()+" "+ safeAppointment.getPatient().getSurname());
        User doctor = userRepository.findById(safeAppointment.getDoctor().getId()).orElse(null);
        if(doctor != null){
            safeAppointmentDTO.setDoctornames(doctor.getFirstnames()+" "+ doctor.getSurname());
        }
        return  safeAppointmentDTO;

    }


}
