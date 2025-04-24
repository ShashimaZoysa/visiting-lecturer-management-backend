package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.VerifiedCourseWorkload;
import com.visitinglecturer.vlms_backend.entity.VisitingLecturer;
import com.visitinglecturer.vlms_backend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VerifiedCourseWorkloadRepository extends JpaRepository<VerifiedCourseWorkload, Long> {

    List<VerifiedCourseWorkload> findByVisitingLecturer(VisitingLecturer visitingLecturer);

    List<VerifiedCourseWorkload> findByCourse(Course course);

    Optional<VerifiedCourseWorkload> findByVisitingLecturerAndCourse(
            VisitingLecturer visitingLecturer,
            Course course
    );
}

