package com.sabujak.gamong.config.security;

import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.exception.HandleJwtException;
import com.sabujak.gamong.exception.InvalidLoginIdException;
import com.sabujak.gamong.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = resolveJwt(request);

        if (jwt != null) {
            try {
                if (jwtUtility.validateJwt(jwt)) {
                    Authentication auth = getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (HandleJwtException | JwtException e) {
                throw new AuthenticationCredentialsNotFoundException("INVALID_TOKEN", e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveJwt(HttpServletRequest request) {
        String authorizationHeader  = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeader.substring(7);
    }

    private Authentication getAuthentication(String jwt) {
        Claims claims = jwtUtility.getClaimsFromJwt(jwt);

        User user = userRepository.findById(Long.valueOf(claims.getSubject()))
                .orElseThrow(InvalidLoginIdException::new);

        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
