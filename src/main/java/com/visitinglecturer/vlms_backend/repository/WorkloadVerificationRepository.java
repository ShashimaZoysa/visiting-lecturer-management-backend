package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.Workload;
import com.visitinglecturer.vlms_backend.entity.WorkloadGroup;
import com.visitinglecturer.vlms_backend.entity.WorkloadVerification;
import com.visitinglecturer.vlms_backend.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkloadVerificationRepository extends JpaRepository<WorkloadVerification, Long> {
    WorkloadVerification findTopByWorkload_IdOrderByVerificationDateDesc(Long workloadId);

    Optional<WorkloadVerification> findByWorkload(Workload workload);
    List<WorkloadVerification> findByWorkload_WorkloadGroupAndVerificationStatus_Status(
            WorkloadGroup group,
            VerificationStatus status
    );


    // You can add custom queries if needed
}

