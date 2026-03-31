package com.github.aliakseisilivonchyk.taskmanager.controller;

import com.github.aliakseisilivonchyk.taskmanager.dto.JwtAuthResponse;
import com.github.aliakseisilivonchyk.taskmanager.dto.SignInRequest;
import com.github.aliakseisilivonchyk.taskmanager.dto.SignUpRequest;
import com.github.aliakseisilivonchyk.taskmanager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody SignUpRequest signUpRequest) {
        authService.register(signUpRequest);
    }

    @PostMapping("/login")
    public JwtAuthResponse login(@RequestBody SignInRequest signInRequest) {
        return authService.login(signInRequest);
    }
}
