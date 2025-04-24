package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.PayeeWorkloadActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayeeWorkloadActivityRepository extends JpaRepository<PayeeWorkloadActivity, Long> {
    // Optional: Add custom methods to fetch activities by PayeeForm ID
}
