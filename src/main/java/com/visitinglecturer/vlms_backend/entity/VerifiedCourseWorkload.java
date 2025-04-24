package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifiedCourseWorkload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "visiting_lecturer_id", nullable = false)
    private VisitingLecturer visitingLecturer;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private double courseLevelVerifiedHours;
}
