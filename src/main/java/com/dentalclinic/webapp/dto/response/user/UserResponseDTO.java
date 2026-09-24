package com.dentalclinic.webapp.dto.response.user;

import com.dentalclinic.webapp.model.Role;

public class UserResponseDTO {
    //used to show only this information when the user registers
    private Long id;
    private String socialsecuritynumber;
    private String firstnames;
    private String surname;
    private String email;
    private Role role;

    public UserResponseDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSocialsecuritynumber() {
        return socialsecuritynumber;
    }

    public void setSocialsecuritynumber(String socialsecuritynumber) {
        this.socialsecuritynumber = socialsecuritynumber;
    }

    public String getFirstnames() {
        return firstnames;
    }

    public void setFirstnames(String firstnames) {
        this.firstnames = firstnames;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
