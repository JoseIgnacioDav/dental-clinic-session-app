package com.dentalclinic.webapp.dto.request.appointment;


import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentRequestDTO {
    private LocalDate date;
    private LocalTime time;
    private Long doctorId;
    private Long patientId;

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
