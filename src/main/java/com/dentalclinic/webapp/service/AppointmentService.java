package com.dentalclinic.webapp.service;

import com.dentalclinic.webapp.dto.request.appointment.AppointmentRequestDTO;
import com.dentalclinic.webapp.dto.response.appointment.AppointmentConfidentialResponseDTO;
import com.dentalclinic.webapp.dto.response.appointment.AppointmentResponseDTO;
import com.dentalclinic.webapp.dto.response.appointment.PatientScheduledAppointmentsDTO;
import com.dentalclinic.webapp.model.Appointment;
import com.dentalclinic.webapp.model.User;
import com.dentalclinic.webapp.repository.AppointmentRepository;
import com.dentalclinic.webapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;


    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository){
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

        //Doctor validation
        if(appointment.getDoctorId() == null){
            throw new RuntimeException("You must specify the Doctor's ID");
        }
        Optional<User> doctorOPT = userRepository.findById(appointment.getDoctorId());
        if(doctorOPT.isEmpty()){
            throw new RuntimeException("The Doctor does not exist");
        }
        safeAppointment.setDoctor(doctorOPT.get());

        //who is creating the date:
        /**
         * If the role is PATIENT ignore the json and set the appointment for himself
         * if the role is DOCTOR or ADMIN third party appointment creation is allowed
         * **/
        if(loggedUser.getRole().equalsIgnoreCase("PATIENT")){
            safeAppointment.setPatient(loggedUser);
        } else if (loggedUser.getRole().equalsIgnoreCase("DOCTOR")||loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
            if (appointment.getPatientId() == null){
                throw new RuntimeException("You must specify the Patient's ID for this appointment");
            }
            //Patient validation
            Optional<User> jsonPatientOPT =userRepository.findById(appointment.getPatientId());
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
    // get all the Patient's Scheduled Appointments
    public List<PatientScheduledAppointmentsDTO>getPAtientScheduledAppointments(Long id){
        List<Appointment>exposedAppointments = appointmentRepository.findAppointmentByPatient_Id(id);
        List<PatientScheduledAppointmentsDTO> safeAppointments = new ArrayList<>();
        for(Appointment exposedAppointment : exposedAppointments){
            PatientScheduledAppointmentsDTO safeAppointment = new PatientScheduledAppointmentsDTO();
            if(exposedAppointment.getDoctor() != null){
                safeAppointment.setDoctorName(exposedAppointment.getDoctor().getFirstnames()+" " + exposedAppointment.getDoctor().getSurname());
            }else {
                safeAppointment.setDoctorName("Unasigned");
            }
            safeAppointment.setTime(exposedAppointment.getTime());
            safeAppointment.setDate(exposedAppointment.getDate());
            safeAppointment.setStatus(exposedAppointment.getStatus());
            safeAppointment.setId(exposedAppointment.getId());
            safeAppointments.add(safeAppointment);
        }
        return safeAppointments;
    }
    /* Return Doctor assigned Appointments incluiding patient confidential information on a given date */
    public List<AppointmentConfidentialResponseDTO> getDoctorsAssignedAppointments(Long doctorId, LocalDate date){
        List<Appointment>appointments = appointmentRepository.findByDoctorAndDate(doctorId,date);
        //Clean and safe list
        List<AppointmentConfidentialResponseDTO>safelist = new ArrayList<>();

        for (Appointment exposedAppointment : appointments){
            AppointmentConfidentialResponseDTO dto = new AppointmentConfidentialResponseDTO();
            dto.setId(exposedAppointment.getId());
            dto.setDate(exposedAppointment.getDate());
            dto.setTime(exposedAppointment.getTime());
            dto.setStatus(exposedAppointment.getStatus());

            //validations
            if (exposedAppointment.getDoctor()!= null){
                dto.setDoctorNames(exposedAppointment.getDoctor().getFirstnames()+" "+exposedAppointment.getDoctor().getSurname());
            }else {
                dto.setDoctorNames("Unassigned Doctor Name");
            }

            if(exposedAppointment.getPatient() != null){
                dto.setPatientNames(exposedAppointment.getPatient().getFirstnames()+" "+ exposedAppointment.getPatient().getSurname());
            }else {
                dto.setPatientNames("Unassigned Patient Name");
            }
            if (exposedAppointment.getPatient() != null) {
                if (exposedAppointment.getPatient().getEmail() != null) {
                    dto.setPatientEmail(exposedAppointment.getPatient().getEmail());
                } else {
                    dto.setPatientEmail(""); // if the patient is null
                }

                if (exposedAppointment.getPatient().getSocialsecuritynumber() != null){
                    dto.setPatientSocialSecurityNumber(exposedAppointment.getPatient().getSocialsecuritynumber());
                } else {
                    dto.setPatientSocialSecurityNumber("");
                }
            } else {
                dto.setPatientEmail("");
                dto.setPatientSocialSecurityNumber("");
            }

            safelist.add(dto);
        }
        return safelist;
    }





}
