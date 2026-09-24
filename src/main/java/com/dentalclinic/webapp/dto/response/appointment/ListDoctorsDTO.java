package com.dentalclinic.webapp.dto.response.appointment;
// List all Doctors
public class ListDoctorsDTO {
    Long doctorId;
    String names;

    public ListDoctorsDTO(){}

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }
}
