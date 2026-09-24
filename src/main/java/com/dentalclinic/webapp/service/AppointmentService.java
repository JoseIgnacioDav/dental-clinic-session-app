package com.dentalclinic.webapp.service;

import com.dentalclinic.webapp.dto.request.appointment.AppointmentRequestDTO;
import com.dentalclinic.webapp.dto.response.appointment.*;
import com.dentalclinic.webapp.dto.response.user.UserResponseDTO;
import com.dentalclinic.webapp.model.Appointment;
import com.dentalclinic.webapp.model.Role;
import com.dentalclinic.webapp.model.User;
import com.dentalclinic.webapp.repository.AppointmentRepository;
import com.dentalclinic.webapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

    public AppointmentResponseDTO createappointment (AppointmentRequestDTO appointment, Long patientIdSession){
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
        if(loggedUser.getRole() == Role.DOCTOR){
            safeAppointment.setPatient(loggedUser);
        } else if (loggedUser.getRole() == Role.DOCTOR||loggedUser.getRole() == Role.ADMIN) {
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
        List<Appointment>appointments = appointmentRepository.findByDoctor_IdAndDate(doctorId,date);
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
    public List<OccupiedAppointmentsPublicResponseDTO>publicversion(Long doctorId, LocalDate date){
        List<Appointment> exposedAppointments = appointmentRepository.findByDoctor_IdAndDate(doctorId, date);
        List<OccupiedAppointmentsPublicResponseDTO>safeList = new ArrayList<>();
        for(Appointment exposedAppointment : exposedAppointments){
            OccupiedAppointmentsPublicResponseDTO dto = new OccupiedAppointmentsPublicResponseDTO();
            dto.setId(exposedAppointment.getId());
            dto.setDate(exposedAppointment.getDate());
            dto.setTime(exposedAppointment.getTime());
            dto.setStatus(exposedAppointment.getStatus());

            if (exposedAppointment.getDoctor()!= null){
                dto.setDoctorNames(exposedAppointment.getDoctor().getFirstnames()+" "+exposedAppointment.getDoctor().getSurname());
            }else {
                dto.setDoctorNames("Unassigned Doctor Name");
            }
            safeList.add(dto);
        }
        return safeList;
    }

    public List<ListDoctorsDTO>listdoctors(){
        List<ListDoctorsDTO>safeList = new ArrayList<>();
        String role = "DOCTOR";
        List<User>exposedList = userRepository.findByRole(role);
        if(exposedList.isEmpty()){
            throw new RuntimeException("There are no Doctors");
        }
        for(User exposedDoctor : exposedList){
            ListDoctorsDTO dto = new ListDoctorsDTO();
            dto.setDoctorId(exposedDoctor.getId());
            dto.setNames(exposedDoctor.getFirstnames()+" "+ exposedDoctor.getSurname());
            safeList.add(dto);
        }
        return safeList;
    }


    public AppointmentResponseDTO updateStatus(Long appointmentID, String newStatus, UserResponseDTO userSession){
        if(userSession == null || userSession.getRole() == null){
            throw new RuntimeException("Access Denied");
        }

        if(!(userSession.getRole() == Role.DOCTOR) && !(userSession.getRole() == Role.ADMIN)){
            throw new RuntimeException("Access Denied");
        }

        Optional<Appointment> appointmentOPT = appointmentRepository.findById(appointmentID);
        if(appointmentOPT.isEmpty()){
            throw new RuntimeException("Unreachable Appointment");
        }

        Appointment appointment = appointmentOPT.get();

        // Role check
        if(userSession.getRole() == Role.DOCTOR){
            if(appointment.getDoctor() == null || !appointment.getDoctor().getId().equals(userSession.getId())){
                throw new RuntimeException("Access Denied");
            }
        }

        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);

        AppointmentResponseDTO safeAppointment = new AppointmentResponseDTO();

        // Validaciones defensivas para el DTO manteniendo tu estilo
        if(appointment.getDoctor() != null){
            safeAppointment.setDoctornames(appointment.getDoctor().getFirstnames() + " " + appointment.getDoctor().getSurname());
        } else {
            safeAppointment.setDoctornames("Unassigned Doctor Name");
        }

        safeAppointment.setStatus(appointment.getStatus());

        if(appointment.getPatient() != null){
            safeAppointment.setPatientnames(appointment.getPatient().getFirstnames() + " " + appointment.getPatient().getSurname());
        } else {
            safeAppointment.setPatientnames("Unassigned Patient Name");
        }

        safeAppointment.setId(appointment.getId());
        safeAppointment.setTime(appointment.getTime());
        safeAppointment.setDate(appointment.getDate());

        return safeAppointment;
    }
}
