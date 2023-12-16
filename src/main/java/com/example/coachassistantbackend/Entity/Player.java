package com.example.coachassistantbackend.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "players")
@SQLDelete(sql = "UPDATE players SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Player extends BaseAuditEntity{
    
    @Column(name = "name")
    @NotEmpty(message = "Name must be not null or empty")
    private String name;

    @Column(name = "surname")
    @NotEmpty(message = "Surname must be not null or empty")
    private String surname;

    @Column(name = "birth_date")
    @NotNull(message = "Birthdate must be not null or empty")
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
