package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.VerifiedGroupWorkload;
import com.visitinglecturer.vlms_backend.entity.VisitingLecturer;
import com.visitinglecturer.vlms_backend.entity.Course;
import com.visitinglecturer.vlms_backend.entity.WorkloadGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerifiedGroupWorkloadRepository extends JpaRepository<VerifiedGroupWorkload, Long> {

    List<VerifiedGroupWorkload> findByVisitingLecturer(VisitingLecturer visitingLecturer);

    List<VerifiedGroupWorkload> findByCourse(Course course);

    List<VerifiedGroupWorkload> findByGroup(WorkloadGroup group);

    List<VerifiedGroupWorkload> findByVisitingLecturerAndCourse(VisitingLecturer lecturer, Course course);


    Optional<VerifiedGroupWorkload> findByVisitingLecturerAndCourseAndGroup(
            VisitingLecturer visitingLecturer,
            Course course,
            WorkloadGroup group
    );
}
