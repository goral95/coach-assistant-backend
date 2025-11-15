package com.example.coachassistantbackend.Util;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class TokenGenerator {

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

}
