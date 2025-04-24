package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.VisitingLecturer;
import com.visitinglecturer.vlms_backend.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitingLecturerRepository extends JpaRepository<VisitingLecturer, Long> {

}

