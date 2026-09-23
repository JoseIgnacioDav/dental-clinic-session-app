package com.dentalclinic.webapp.dto.request.user;

public class UserRequestDTO {
    private String socialsecuritynumber;
    private String firstnames;
    private String surname;
    private String email;
    private String password;

    public UserRequestDTO(){}

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
