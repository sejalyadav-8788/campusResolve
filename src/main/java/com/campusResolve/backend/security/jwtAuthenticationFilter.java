package com.campusResolve.backend.security;

import com.campusResolve.backend.entity.user;
import com.campusResolve.backend.repository.userRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class jwtAuthenticationFilter extends OncePerRequestFilter {

    private final jwtService jwtService;
    private final userRepository userRepository;

    public jwtAuthenticationFilter(
            jwtService jwtService,
            userRepository userRepository) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        System.out.println("Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            String email = jwtService.extractEmail(token);

            System.out.println("JWT email: " + email);

            user existingUser = userRepository.findByEmail(email)
                    .orElse(null);

            if (existingUser != null) {

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(
                                "ROLE_" + existingUser.getRole()
                        );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                existingUser.getEmail(),
                                null,
                                List.of(authority)
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                System.out.println("JWT authentication successful for: "
                        + existingUser.getEmail());
            }

        } catch (Exception e) {

            System.out.println("JWT ERROR: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}