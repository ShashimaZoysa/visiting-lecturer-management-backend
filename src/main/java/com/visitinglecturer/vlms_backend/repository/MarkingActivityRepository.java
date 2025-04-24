package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.MarkingActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkingActivityRepository extends JpaRepository<MarkingActivity, Long> {
    // Custom query methods can be added here if needed, for example:
    // Optional<MarkingActivity> findByActivityName(String activityName);
}
