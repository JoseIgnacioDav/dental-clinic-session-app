package com.dentalclinic.webapp.dto.response.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentResponse {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private String status;
    private String doctornames;
    private String patientnames;

    public AppointmentResponse(){}

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

    public String getDoctornames() {
        return doctornames;
    }

    public void setDoctornames(String doctornames) {
        this.doctornames = doctornames;
    }

    public String getPatientnames() {
        return patientnames;
    }

    public void setPatientnames(String patientnames) {
        this.patientnames = patientnames;
    }
}
