package com.dentalclinic.webapp.dto.response.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

// this dto returns patient's confindential information linked to a doctor's assigned appointments
public class AppointmentConfidentialResponseDTO {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private String status;
    //sensible information:
    private String doctorNames;
    private String patientNames;
    private String patientSocialSecurityNumber;
    private String patientEmail;

    public AppointmentConfidentialResponseDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDoctorNames() {
        return doctorNames;
    }

    public void setDoctorNames(String doctorNames) {
        this.doctorNames = doctorNames;
    }

    public String getPatientNames() {
        return patientNames;
    }

    public void setPatientNames(String patientNames) {
        this.patientNames = patientNames;
    }

    public String getPatientSocialSecurityNumber() {
        return patientSocialSecurityNumber;
    }

    public void setPatientSocialSecurityNumber(String patientSocialSecurityNumber) {
        this.patientSocialSecurityNumber = patientSocialSecurityNumber;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }
}
