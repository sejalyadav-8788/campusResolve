package com.campusResolve.backend.service;

import com.campusResolve.backend.dto.loginRequest;
import com.campusResolve.backend.dto.signupRequest;
import com.campusResolve.backend.entity.user;
import com.campusResolve.backend.repository.userRepository;
import com.campusResolve.backend.security.jwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class userService {

    private final userRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final jwtService jwtService;

    public userService(userRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       jwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public user registerUser(signupRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        user newUser = new user(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole(),
                request.getDepartment()
        );

        return userRepository.save(newUser);
    }

    public String loginUser(loginRequest request) {

        user existingUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                existingUser.getPassword())) {

            throw new RuntimeException("Invalid email or password");
        }

        return jwtService.generateToken(
                existingUser.getEmail(),
                existingUser.getRole()
        );
    }
}