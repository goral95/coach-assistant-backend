package com.example.coachassistantbackend.Entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class User extends BaseAuditEntity implements UserDetails {

    @Column(name = "name")
    @NotEmpty(message = "Name must be not null or empty")
    private String name;

    @Column(name = "surname")
    @NotEmpty(message = "Surname must be not null or empty")
    private String surname;

    @Column(name = "birth_date")
    @NotNull(message = "Birthdate must be not null or empty")
    private LocalDateTime birthDate;

    @Column(name = "email")
    @NotNull(message = "Birthdate must be not null or empty")
    private String email;

    @Column(name = "password")
    @NotNull(message = "Birthdate must be not null or empty")
    private String password;

    @Column(name = "license")
    private String license;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
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

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
