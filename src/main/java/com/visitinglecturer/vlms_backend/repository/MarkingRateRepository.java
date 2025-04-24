package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.MarkingRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkingRateRepository extends JpaRepository<MarkingRate, Long> {
    // Custom query methods can be added here, for example:
    // Optional<MarkingRate> findByMarkingTypeAndMarkingActivity(MarkingType markingType, MarkingActivity markingActivity);
}

