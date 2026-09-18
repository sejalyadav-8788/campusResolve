package com.campusResolve.backend.service;

import com.campusResolve.backend.entity.complaint;
import com.campusResolve.backend.entity.user;
import com.campusResolve.backend.repository.complaintRepository;
import com.campusResolve.backend.repository.userRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class escalationService {

    private final complaintRepository complaintRepository;
    private final userRepository userRepository;

    public escalationService(
            complaintRepository complaintRepository,
            userRepository userRepository) {

        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void checkOverdueComplaints() {

        List<complaint> complaints = complaintRepository.findAll();

        for (complaint c : complaints) {

            if (c.getDeadline() != null
                    && c.getDeadline().isBefore(LocalDateTime.now())
                    && !c.getStatus().equals("RESOLVED")
                    && !c.getStatus().equals("CLOSED")) {

                escalateComplaint(c);
            }
        }
    }

    private void escalateComplaint(complaint c) {

        user nextAuthority = null;

        if (c.getEscalationLevel() == 0) {

            nextAuthority = userRepository
                    .findByRole("DEAN")
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (nextAuthority != null) {
                c.setEscalationLevel(1);
            }

        } else if (c.getEscalationLevel() == 1) {

            nextAuthority = userRepository
                    .findByRole("ADMIN")
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (nextAuthority != null) {
                c.setEscalationLevel(2);
            }
        }

        if (nextAuthority != null) {

            c.setAssignedAuthorityId(nextAuthority.getId());
            c.setStatus("ESCALATED");

            // Give the next authority a fresh deadline
            c.setDeadline(
                    LocalDateTime.now().plusHours(24)
            );

            complaintRepository.save(c);

            System.out.println(
                    "Complaint " + c.getId()
                            + " escalated to "
                            + nextAuthority.getRole()
            );
        }
    }
}