package com.campusResolve.backend.controller;

import com.campusResolve.backend.dto.complaintRequest;
import com.campusResolve.backend.entity.complaint;
import com.campusResolve.backend.service.complaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin("*")
public class complaintController {

    private final complaintService complaintService;

    public complaintController(complaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping("/test")
    public String test(Authentication authentication) {

        return "Welcome " + authentication.getName()
                + "! JWT authentication is working.";
    }

    @PostMapping
    public ResponseEntity<complaint> createComplaint(
            @RequestBody complaintRequest request,
            Authentication authentication) {

        complaint newComplaint = complaintService.createComplaint(
                request,
                authentication.getName()
        );

        return ResponseEntity.ok(newComplaint);
    }
    @GetMapping("/my")
    public ResponseEntity<java.util.List<complaint>> getMyComplaints(
            Authentication authentication) {

        java.util.List<complaint> complaints =
                complaintService.getMyComplaints(authentication.getName());

        return ResponseEntity.ok(complaints);
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<complaint> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication) {

        complaint updatedComplaint =
                complaintService.updateStatus(
                        id,
                        status,
                        authentication.getName()
                );

        return ResponseEntity.ok(updatedComplaint);
    }
}