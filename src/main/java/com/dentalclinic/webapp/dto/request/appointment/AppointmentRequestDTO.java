package com.dentalclinic.webapp.dto.request.appointment;

import com.dentalclinic.webapp.model.User;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentRequestDTO {
    private LocalDate date;
    private LocalTime time;
    private User doctor;
    private User patient;

    public AppointmentRequestDTO(){}

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

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }
}
