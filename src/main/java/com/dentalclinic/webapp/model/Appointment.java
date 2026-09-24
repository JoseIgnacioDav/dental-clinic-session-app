package com.dentalclinic.webapp.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointmens",uniqueConstraints = { // race condition and duplicates protection
        @UniqueConstraint(columnNames = {"doctor_id","date","time"})
})
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //appointment information
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private LocalTime time;
    @Column(nullable = false)
    private String status = "PENDING";
    @Column(nullable = false,updatable = false)
    private LocalTime creationtime;
    @Column(nullable = false,updatable = false)
    private LocalDate creationdate;
    @PrePersist
    protected void onCreate(){ // set automatically
        this.creationdate = LocalDate.now();
        this.creationtime = LocalTime.now();
    }
    //relations to User
    //Doctor
    @ManyToOne
    @JoinColumn(name = "doctor_id",nullable = false)
    private User doctor;

    //Patient
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;
    //constructor
    public Appointment(){}

    //getters and setters


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

    public LocalTime getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(LocalTime creationtime) {
        this.creationtime = creationtime;
    }

    public LocalDate getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(LocalDate creationdate) {
        this.creationdate = creationdate;
    }
}
