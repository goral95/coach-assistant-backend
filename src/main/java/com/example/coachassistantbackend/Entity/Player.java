package com.example.coachassistantbackend.Entity;

import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "players")
public class Player extends BaseAuditEntity{
    
    @Column(name = "name")
    @NotBlank(message = "Name must be not null or empty")
    private String name;

    @Column(name = "surname")
    @NotBlank(message = "Surname must be not null or empty")
    private String surname;

    @Column(name = "birth_date")
    @JsonFormat(pattern="YYYY-MM-dd")
    @NotBlank(message = "Birthdate must be not null or empty")
    private LocalDateTime birthDate;

    @Column(name = "footed")
    private String footed;

    @Column(name = "position")
    private String position;

    @Column(name = "city")
    private String city;

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

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getFooted() {
        return footed;
    }

    public void setFooted(String footed) {
        this.footed = footed;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
