package com.github.aliakseisilivonchyk.taskmanager.filter;

import com.github.aliakseisilivonchyk.taskmanager.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_HEADER_BEARER = "Bearer ";
    private static final String CREDENTIALS_EXPIRED_MESSAGE = "Время действия токена истекло.";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith(AUTH_HEADER_BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtString = authHeader.substring(AUTH_HEADER_BEARER.length());

        if (isTokenNonExpired(jwtService.getExpirationTimeTokenClaim(jwtString))) {
            String usernameClaim = jwtService.getUsernameTokenClaim(jwtString);
            UserDetails userDetails = userDetailsService.loadUserByUsername(usernameClaim);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } else {
            throw new CredentialsExpiredException(CREDENTIALS_EXPIRED_MESSAGE);
        }
    }

    private boolean isTokenNonExpired(Date expirationTime) {
        return expirationTime.after(new Date());
    }
}
