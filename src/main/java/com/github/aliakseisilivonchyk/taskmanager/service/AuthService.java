package com.github.aliakseisilivonchyk.taskmanager.service;

import com.github.aliakseisilivonchyk.taskmanager.dto.JwtAuthResponse;
import com.github.aliakseisilivonchyk.taskmanager.dto.SignInRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthResponse login(SignInRequest signInRequest) {
        Authentication unauthenticated = new UsernamePasswordAuthenticationToken(signInRequest.username(),
                signInRequest.password());
        Authentication authenticated = authenticationManager.authenticate(unauthenticated);
        UserDetails userDetails = (UserDetails) authenticated.getPrincipal();

        return new JwtAuthResponse(jwtService.generateToken(userDetails));
    }
}
