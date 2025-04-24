package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
    // Custom queries can be added here
}

