package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.MarkingRateType;
import com.visitinglecturer.vlms_backend.entity.MarkingTypeRateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkingTypeRateTypeRepository extends JpaRepository<MarkingTypeRateType, Long> {

    @Query("SELECT m.markingRateType FROM MarkingTypeRateType m WHERE m.markingType.markingTypeId = ?1")
    List<MarkingRateType> findRateTypesByMarkingType(Long markingTypeId);
}


