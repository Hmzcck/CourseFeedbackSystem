package com.webapp.coursefeedbacksystem.controller;

import com.webapp.coursefeedbacksystem.dto.*;
import com.webapp.coursefeedbacksystem.model.UserModel;
import com.webapp.coursefeedbacksystem.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;


@RestController
public class UserController {
    private final AuthenticationService authService;
    private final UserService userService;
    private final RestTemplate template;
    private final JwtService jwtService;

    @Value("gpt-3.5-turbo")
    private String model;

    @Value(("https://api.openai.com/v1/chat/completions"))
    private String apiURL;

    public UserController(AuthenticationService authService, UserService userService, 
            RestTemplate template, JwtService jwtService) {
        this.authService = authService;
        this.userService = userService;
        this.template = template;
        this.jwtService = jwtService;
    }



    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UserModel request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody UserModel request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
