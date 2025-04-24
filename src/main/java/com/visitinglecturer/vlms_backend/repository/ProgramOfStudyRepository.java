package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.ProgramOfStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramOfStudyRepository extends JpaRepository<ProgramOfStudy, Long> {
    // Optional<ProgramOfStudy> findByProgramName(String programName);
}

