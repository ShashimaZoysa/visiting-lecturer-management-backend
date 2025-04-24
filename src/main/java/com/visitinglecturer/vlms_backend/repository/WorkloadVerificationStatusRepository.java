package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.WorkloadVerificationStatus;
import com.visitinglecturer.vlms_backend.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkloadVerificationStatusRepository extends JpaRepository<WorkloadVerificationStatus, Long> {
    // You can add custom queries if needed
    Optional<WorkloadVerificationStatus> findByStatus(VerificationStatus status);
}

