package com.github.aliakseisilivonchyk.taskmanager.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
public class JwtService {

    private static final String JWT_TOKEN_TYPE = "JWT";
    private static final String USERNAME_CLAIM = "username";
    private static final String ROLE_CLAIM = "role";
    private static final String EXPIRATION_TIME_CLAIM = "expirationTime";

    @Value("${jwt.token.expiration}")
    private Duration jwtTokenExpiration;

    public String generateToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get();
        Date expirationDate = new Date(System.currentTimeMillis() + jwtTokenExpiration.toMillis());

        return Jwts.builder()
                .header().type(JWT_TOKEN_TYPE).and()
                .claim(USERNAME_CLAIM, userDetails.getUsername())
                .claim(ROLE_CLAIM, role)
                .claim(EXPIRATION_TIME_CLAIM, expirationDate)
                .compact();
    }

    public boolean isTokenValid(Claims claims) {
        return claims.get(EXPIRATION_TIME_CLAIM, Date.class).after(new Date());
    }
}
