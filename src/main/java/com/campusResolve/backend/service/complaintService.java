package com.campusResolve.backend.service;

import com.campusResolve.backend.dto.complaintRequest;
import com.campusResolve.backend.entity.complaint;
import com.campusResolve.backend.entity.user;
import com.campusResolve.backend.repository.complaintRepository;
import com.campusResolve.backend.repository.userRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class complaintService {

    private final complaintRepository complaintRepository;
    private final userRepository userRepository;
    private final s3Service s3Service;

    public complaintService(
            complaintRepository complaintRepository,
            userRepository userRepository,
            s3Service s3Service) {

        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }

    public complaint createComplaint(
            complaintRequest request,
            String studentEmail,
            MultipartFile file) {

        user student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        complaint newComplaint = new complaint();

        newComplaint.setTitle(request.getTitle());
        newComplaint.setDescription(request.getDescription());
        newComplaint.setCategory(request.getCategory());
        newComplaint.setSubcategory(request.getSubcategory());
        newComplaint.setPriority(request.getPriority());
        newComplaint.setSensitive(request.isSensitive());
        newComplaint.setDepartment(request.getDepartment());
        newComplaint.setLocation(request.getLocation());

        // Upload attachment to AWS S3
        if (file != null && !file.isEmpty()) {

            try {

                String attachmentKey =
                        s3Service.uploadFile(file);

                newComplaint.setAttachmentKey(
                        attachmentKey
                );

            } catch (IOException e) {

                throw new RuntimeException(
                        "File upload failed"
                );
            }
        }

        newComplaint.setStatus("OPEN");

        newComplaint.setCreatedAt(
                LocalDateTime.now()
        );

        // Complaint has 24 hours to be resolved
        newComplaint.setDeadline(
                LocalDateTime.now().plusHours(24)
        );

        newComplaint.setStudentId(
                student.getId()
        );

        // Automatic routing
        user authority;

        if (request.isSensitive()) {

            // Sensitive complaints go directly to Dean
            authority = userRepository
                    .findByRole("DEAN")
                    .stream()
                    .findFirst()
                    .orElse(null);

        } else {

            // Normal complaints go to HOD/FACULTY
            // of selected department
            authority = userRepository
                    .findByDepartment(
                            request.getDepartment()
                    )
                    .stream()
                    .filter(u ->
                            u.getRole().equals("HOD")
                                    || u.getRole().equals("FACULTY"))
                    .findFirst()
                    .orElse(null);
        }

        // Assign complaint to authority
        if (authority != null) {

            newComplaint.setAssignedAuthorityId(
                    authority.getId()
            );

            newComplaint.setStatus("ASSIGNED");
        }

        return complaintRepository.save(
                newComplaint
        );
    }

    public List<complaint> getMyComplaints(
            String studentEmail) {

        user student = userRepository
                .findByEmail(studentEmail)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        return complaintRepository
                .findByStudentId(student.getId());
    }

    public List<complaint> getAssignedComplaints(
            String authorityEmail) {

        user authority = userRepository
                .findByEmail(authorityEmail)
                .orElseThrow(() ->
                        new RuntimeException("Authority not found"));

        return complaintRepository
                .findByAssignedAuthorityId(
                        authority.getId()
                );
    }

    public complaint updateStatus(
            Long complaintId,
            String status,
            String authorityEmail) {

        user authority = userRepository
                .findByEmail(authorityEmail)
                .orElseThrow(() ->
                        new RuntimeException("Authority not found"));

        complaint existingComplaint =
                complaintRepository
                        .findById(complaintId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Complaint not found"
                                ));

        if (!authority.getId().equals(
                existingComplaint.getAssignedAuthorityId())) {

            throw new RuntimeException(
                    "You are not authorized to update this complaint"
            );
        }

        existingComplaint.setStatus(status);

        return complaintRepository.save(
                existingComplaint
        );
    }

    public List<complaint> getAllComplaints() {

        return complaintRepository.findAll();
    }

    public long getTotalComplaints() {

        return complaintRepository.count();
    }

    public long getOpenComplaints() {

        return complaintRepository.countByStatus("OPEN");
    }

    public long getAssignedComplaintsCount() {

        return complaintRepository.countByStatus("ASSIGNED");
    }

    public long getEscalatedComplaints() {

        return complaintRepository.countByStatus("ESCALATED");
    }

    public long getResolvedComplaints() {

        return complaintRepository.countByStatus("RESOLVED");
    }
}