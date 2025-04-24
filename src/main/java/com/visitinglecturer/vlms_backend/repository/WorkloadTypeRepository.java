package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.WorkloadType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkloadTypeRepository extends JpaRepository<WorkloadType, Long> {
    // You can add custom queries if needed
}

