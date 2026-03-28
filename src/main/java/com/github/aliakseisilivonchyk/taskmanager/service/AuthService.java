package com.github.aliakseisilivonchyk.taskmanager.service;

import com.github.aliakseisilivonchyk.taskmanager.dto.JwtAuthResponse;
import com.github.aliakseisilivonchyk.taskmanager.dto.SignInRequest;
import com.github.aliakseisilivonchyk.taskmanager.dto.SignUpRequest;
import com.github.aliakseisilivonchyk.taskmanager.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtAuthResponse register(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.username());
        user.setEmail(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setRole(signUpRequest.role());

        userService.create(user);

        return new JwtAuthResponse(jwtService.generateToken(user));
    }

    public JwtAuthResponse login(SignInRequest signInRequest) {
        Authentication unauthenticated = new UsernamePasswordAuthenticationToken(signInRequest.username(),
                signInRequest.password());
        Authentication authenticated = authenticationManager.authenticate(unauthenticated);
        UserDetails userDetails = (UserDetails) authenticated.getPrincipal();

        return new JwtAuthResponse(jwtService.generateToken(userDetails));
    }
}
