package com.campusResolve.backend.repository;

import com.campusResolve.backend.entity.complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface complaintRepository extends JpaRepository<complaint, Long> {

    List<complaint> findByStudentId(Long studentId);

    List<complaint> findByAssignedAuthorityId(Long authorityId);

    List<complaint> findByDepartment(String department);
    long countByStatus(String status);

    long countByStatusIn(List<String> statuses);
}
