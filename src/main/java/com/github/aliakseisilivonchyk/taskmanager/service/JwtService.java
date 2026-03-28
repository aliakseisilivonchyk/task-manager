package com.github.aliakseisilivonchyk.taskmanager.service;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    public String generateToken(UserDetails userDetails) {

        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get();
        Date currentDate = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);

        return Jwts.builder()
                .header().type("JWT").and()
                .claim("username", userDetails.getUsername())
                .claim("role", role)
                .issuedAt(currentDate)
                .expiration(expirationDate)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}
