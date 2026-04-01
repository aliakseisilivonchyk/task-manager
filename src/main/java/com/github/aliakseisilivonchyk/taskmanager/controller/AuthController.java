package com.github.aliakseisilivonchyk.taskmanager.controller;

import com.github.aliakseisilivonchyk.taskmanager.dto.JwtAuthResponse;
import com.github.aliakseisilivonchyk.taskmanager.dto.SignInRequest;
import com.github.aliakseisilivonchyk.taskmanager.dto.UserRequest;
import com.github.aliakseisilivonchyk.taskmanager.dto.UserResponse;
import com.github.aliakseisilivonchyk.taskmanager.service.AuthService;
import com.github.aliakseisilivonchyk.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    public UserResponse register(@RequestBody UserRequest userRequest) {
        return userService.create(userRequest);
    }

    @PostMapping("/login")
    @Operation(summary = "Вход (получение JWT)")
    public JwtAuthResponse login(@RequestBody SignInRequest signInRequest) {
        return authService.login(signInRequest);
    }
}
