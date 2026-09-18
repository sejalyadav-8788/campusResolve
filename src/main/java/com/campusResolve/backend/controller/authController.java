package com.campusResolve.backend.controller;

import com.campusResolve.backend.dto.loginRequest;
import com.campusResolve.backend.dto.signupRequest;
import com.campusResolve.backend.entity.user;
import com.campusResolve.backend.service.userService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")

public class authController {

    private final userService userService;

    public authController(userService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<user> signup(@RequestBody signupRequest request) {

        user newUser = userService.registerUser(request);

        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody loginRequest request) {

        String token = userService.loginUser(request);

        return ResponseEntity.ok(token);
    }

}