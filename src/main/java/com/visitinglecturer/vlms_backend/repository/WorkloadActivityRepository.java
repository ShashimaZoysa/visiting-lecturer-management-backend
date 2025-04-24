package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.WorkloadActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkloadActivityRepository extends JpaRepository<WorkloadActivity, Long> {
    // You can add custom queries if needed
    // Query to get all activities (findAll is already provided by JpaRepository)
    //List<WorkloadActivity> findAll();

    // Query to find an activity by its ID
    //Optional<WorkloadActivity> findById(Long id);
}

