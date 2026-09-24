package com.dentalclinic.webapp.repository;

import com.dentalclinic.webapp.model.Appointment;
import com.dentalclinic.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    // to find all appointments for a specific doctor on a given date
    List<Appointment> findByDoctorAndDate (User doctor, LocalDate date);
    // to find the appointment history of a patient
    List<Appointment> findAppointmentByPatient_Id(Long patientId);
    // to check if there is an Appointment already for a doctor at a specific date and time
    boolean existsByDoctorAndDateAndTime(User doctor, LocalDate date, LocalTime time);

}
