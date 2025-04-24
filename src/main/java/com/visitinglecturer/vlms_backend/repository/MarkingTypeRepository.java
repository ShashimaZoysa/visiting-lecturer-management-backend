package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.MarkingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkingTypeRepository extends JpaRepository<MarkingType, Long> {
    // You can add custom query methods here if needed, for example:
    // Optional<MarkingType> findByTypeName(String typeName);
}

