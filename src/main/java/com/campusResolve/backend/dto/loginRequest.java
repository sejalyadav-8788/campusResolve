package com.campusResolve.backend.dto;

public class loginRequest {

    private String email;
    private String password;

    public loginRequest() {
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