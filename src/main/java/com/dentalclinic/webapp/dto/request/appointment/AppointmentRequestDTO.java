package com.dentalclinic.webapp.dto.request.appointment;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentRequestDTO {
    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date cannot be created in the past")
    private LocalDate date;
    @NotNull(message = "Time is required")
    private LocalTime time;
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    private Long patientId; // only used by doctors and admins

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

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
