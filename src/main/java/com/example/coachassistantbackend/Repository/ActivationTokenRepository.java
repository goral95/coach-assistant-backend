package com.example.coachassistantbackend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.coachassistantbackend.Entity.ActivationToken;

@Repository
@Transactional(readOnly = true)
public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Long> {

    Optional<ActivationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ActivationToken t " +
            "SET t.confirmed = true " +
            "WHERE t.token = ?1")
    int confirmToken(String token);

}