package com.campusResolve.backend.controller;

import com.campusResolve.backend.entity.complaint;
import com.campusResolve.backend.service.complaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authority")
@CrossOrigin("*")
public class authorityController {

    private final complaintService complaintService;

    public authorityController(complaintService complaintService) {
        this.complaintService = complaintService;
    }

    // HOD, Faculty, Dean and Admin can see their assigned complaints
    @PreAuthorize("hasAnyRole('HOD', 'FACULTY', 'DEAN', 'ADMIN')")
    @GetMapping("/complaints")
    public ResponseEntity<List<complaint>> getAssignedComplaints(
            Authentication authentication) {

        List<complaint> complaints =
                complaintService.getAssignedComplaints(
                        authentication.getName()
                );

        return ResponseEntity.ok(complaints);
    }

    // Only Admin can see all complaints
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-complaints")
    public ResponseEntity<List<complaint>> getAllComplaints() {

        List<complaint> complaints =
                complaintService.getAllComplaints();

        return ResponseEntity.ok(complaints);
    }

    // Admin dashboard - total complaints
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard/total")
    public ResponseEntity<Long> getTotalComplaints() {

        long total =
                complaintService.getTotalComplaints();

        return ResponseEntity.ok(total);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard/open")
    public ResponseEntity<Long> getOpenComplaints() {
        return ResponseEntity.ok(
                complaintService.getOpenComplaints()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard/assigned")
    public ResponseEntity<Long> getAssignedComplaintsCount() {
        return ResponseEntity.ok(
                complaintService.getAssignedComplaintsCount()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard/escalated")
    public ResponseEntity<Long> getEscalatedComplaints() {
        return ResponseEntity.ok(
                complaintService.getEscalatedComplaints()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard/resolved")
    public ResponseEntity<Long> getResolvedComplaints() {
        return ResponseEntity.ok(
                complaintService.getResolvedComplaints()
        );
    }
}