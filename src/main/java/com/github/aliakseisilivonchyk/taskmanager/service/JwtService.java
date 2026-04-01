package com.github.aliakseisilivonchyk.taskmanager.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    public String generateToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get();
        Date expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);

        return Jwts.builder()
                .header().type("JWT").and()
                .claim("username", userDetails.getUsername())
                .claim("role", role)
                .claim("expirationTime", expirationDate)
                .compact();
    }

    public boolean isTokenValid(Claims claims) {
        return claims.get("expirationTime", Date.class).after(new Date());
    }
}
