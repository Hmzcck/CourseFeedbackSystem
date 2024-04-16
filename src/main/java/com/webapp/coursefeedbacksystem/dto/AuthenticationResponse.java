package com.webapp.coursefeedbacksystem.dto;

import lombok.Getter;

@Getter
public class AuthenticationResponse {
    private final String token;
    private final String message;

    public AuthenticationResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

}
