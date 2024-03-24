package com.example.coachassistantbackend.Auth;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;

public class RegisterRequest {

    private String name;
    private String surname;
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    private LocalDateTime birthDate;
    private String license;

    public RegisterRequest(String name, String surname, String email, String password, LocalDateTime birthDate,
            String license) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.license = license;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

}
