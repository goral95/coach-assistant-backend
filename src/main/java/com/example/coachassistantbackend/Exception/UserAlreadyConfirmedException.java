package com.example.coachassistantbackend.Exception;

public class UserAlreadyConfirmedException extends RuntimeException {
    public UserAlreadyConfirmedException(String message) {
        super(message);
    }
}
