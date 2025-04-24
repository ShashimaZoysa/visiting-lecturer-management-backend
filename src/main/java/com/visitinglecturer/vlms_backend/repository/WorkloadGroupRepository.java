package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.WorkloadGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkloadGroupRepository extends JpaRepository<WorkloadGroup, Long> {
    // You can add custom queries if needed
    List<WorkloadGroup> findByCourse_Id(Long courseId);
    Optional<WorkloadGroup> findByGroupNumberAndCourseId(String groupNumber, Long courseId);
    List<WorkloadGroup> findByRecommendationForm_ReferenceNumber(String referenceNumber);
}

