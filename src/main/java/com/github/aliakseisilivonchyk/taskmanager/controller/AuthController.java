package com.github.aliakseisilivonchyk.taskmanager.controller;

import com.github.aliakseisilivonchyk.taskmanager.dto.JwtAuthResponse;
import com.github.aliakseisilivonchyk.taskmanager.dto.SignInRequest;
import com.github.aliakseisilivonchyk.taskmanager.dto.UserRequest;
import com.github.aliakseisilivonchyk.taskmanager.dto.UserResponse;
import com.github.aliakseisilivonchyk.taskmanager.service.AuthService;
import com.github.aliakseisilivonchyk.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody UserRequest userRequest) {
        return userService.create(userRequest);
    }

    @PostMapping("/login")
    public JwtAuthResponse login(@RequestBody SignInRequest signInRequest) {
        return authService.login(signInRequest);
    }
}
