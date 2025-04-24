package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.MarkingRateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkingRateTypeRepository extends JpaRepository<MarkingRateType, Long> {
    // Custom query methods can be added here, for example:
    // Optional<MarkingRateType> findByRateTypeName(String rateTypeName);
}

