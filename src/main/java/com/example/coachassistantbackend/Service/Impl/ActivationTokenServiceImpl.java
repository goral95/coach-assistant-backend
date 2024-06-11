package com.example.coachassistantbackend.Service.Impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.coachassistantbackend.Entity.ActivationToken;
import com.example.coachassistantbackend.Repository.ActivationTokenRepository;
import com.example.coachassistantbackend.Service.ActivationTokenService;

@Service
public class ActivationTokenServiceImpl implements ActivationTokenService {

    private final ActivationTokenRepository activationTokenRepository;

    public ActivationTokenServiceImpl(ActivationTokenRepository activationTokenRepository) {
        this.activationTokenRepository = activationTokenRepository;
    }

    public ActivationToken save(ActivationToken activationToken) {
        return activationTokenRepository.save(activationToken);
    }

    public Optional<ActivationToken> getToken(String token) {
        return activationTokenRepository.findByToken(token);
    }

    @Transactional
    public void confirmToken(String token) {
        activationTokenRepository.confirmToken(token);
    }

}
