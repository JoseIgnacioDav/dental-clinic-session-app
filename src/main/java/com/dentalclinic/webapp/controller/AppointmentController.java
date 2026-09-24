package com.dentalclinic.webapp.controller;

import com.dentalclinic.webapp.dto.request.appointment.AppointmentRequestDTO;
import com.dentalclinic.webapp.dto.response.appointment.*;
import com.dentalclinic.webapp.dto.response.user.UserResponseDTO;
import com.dentalclinic.webapp.model.Role;
import com.dentalclinic.webapp.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Create appointment endpoint
    @PostMapping("/create")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@Valid @RequestBody AppointmentRequestDTO requestDTO, HttpSession session) {
        UserResponseDTO loggedUser = (UserResponseDTO) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            throw new RuntimeException("Unauthorized: No active session");
        }
        AppointmentResponseDTO response = appointmentService.createappointment(requestDTO, loggedUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get patient's scheduled appointments
    @GetMapping("/patient/my-appointments")
    public ResponseEntity<List<PatientScheduledAppointmentsDTO>> getPatientAppointments(HttpSession session) {
        UserResponseDTO loggedUser = (UserResponseDTO) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            throw new RuntimeException("Unauthorized: No active session");
        }
        List<PatientScheduledAppointmentsDTO> appointments = appointmentService.getPAtientScheduledAppointments(loggedUser.getId());
        return ResponseEntity.ok(appointments);
    }

    // Get doctor's assigned appointments (confidential)
    @GetMapping("/doctor/assigned")
    public ResponseEntity<List<AppointmentConfidentialResponseDTO>> getDoctorAppointments(
            @RequestParam Long doctorId,
            @RequestParam LocalDate date,
            HttpSession session) {

        UserResponseDTO loggedUser = (UserResponseDTO) session.getAttribute("loggedUser");
        if (loggedUser == null || (!(loggedUser.getRole()== Role.DOCTOR) && !(loggedUser.getRole()== Role.ADMIN))) {
            throw new RuntimeException("Access Denied");
        }

        // If DOCTOR, force their own ID
        if (loggedUser.getRole() == Role.DOCTOR) {
            doctorId = loggedUser.getId();
        }

        List<AppointmentConfidentialResponseDTO> appointments = appointmentService.getDoctorsAssignedAppointments(doctorId, date);
        return ResponseEntity.ok(appointments);
    }

    // Get public occupied appointments
    @GetMapping("/public/occupied")
    public ResponseEntity<List<OccupiedAppointmentsPublicResponseDTO>> getPublicAppointments(
            @RequestParam Long doctorId,
            @RequestParam LocalDate date) {
        List<OccupiedAppointmentsPublicResponseDTO> appointments = appointmentService.publicversion(doctorId, date);
        return ResponseEntity.ok(appointments);
    }

    // List all doctors
    @GetMapping("/doctors")
    public ResponseEntity<List<ListDoctorsDTO>> listDoctors() {
        List<ListDoctorsDTO> doctors = appointmentService.listdoctors();
        return ResponseEntity.ok(doctors);
    }

    // Update appointment status
    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            HttpSession session) {

        UserResponseDTO loggedUser = (UserResponseDTO) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            throw new RuntimeException("Unauthorized: No active session");
        }

        String newStatus = body.get("status");
        AppointmentResponseDTO updated = appointmentService.updateStatus(id, newStatus, loggedUser);
        return ResponseEntity.ok(updated);
    }
}