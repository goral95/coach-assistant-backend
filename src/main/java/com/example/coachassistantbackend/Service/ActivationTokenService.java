package com.example.coachassistantbackend.Service;

import java.util.Optional;

import com.example.coachassistantbackend.Entity.ActivationToken;

public interface ActivationTokenService {

    ActivationToken save(ActivationToken activationToken);

    Optional<ActivationToken> getToken(String token);

    void confirmToken(String token);
}
