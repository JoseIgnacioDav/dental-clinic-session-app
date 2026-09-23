package com.dentalclinic.webapp.dto.request.auth;

public class LoginRequestDTO {
    String email;
    String password;

    public LoginRequestDTO(){}

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
