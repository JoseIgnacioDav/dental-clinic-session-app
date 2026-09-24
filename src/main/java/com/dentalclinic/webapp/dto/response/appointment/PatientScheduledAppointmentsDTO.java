package com.dentalclinic.webapp.dto.response.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public class PatientScheduledAppointmentsDTO {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private String doctorName;
    private String status;

    public PatientScheduledAppointmentsDTO() {}

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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
