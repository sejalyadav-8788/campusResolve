package com.campusResolve.backend.controller;

import com.campusResolve.backend.service.s3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@CrossOrigin("*")
public class fileController {

    private final s3Service s3Service;

    public fileController(s3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file) {

        try {
            String fileName = s3Service.uploadFile(file);

            return ResponseEntity.ok(
                    "File uploaded successfully: " + fileName
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("File upload failed: " + e.getMessage());
        }
    }
}