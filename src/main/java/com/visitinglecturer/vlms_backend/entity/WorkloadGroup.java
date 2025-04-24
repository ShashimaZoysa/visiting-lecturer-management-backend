package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkloadGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupNumber; // Group number (e.g., Group 1, Group 2)

    //private String referenceNumber;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // Foreign key to Course entity

    @ManyToOne
    @JoinColumn(name = "visiting_lecturer_id", nullable = false)
    private VisitingLecturer visitingLecturer; // Foreign key to VisitingLecturer entity

    @ManyToOne
    @JoinColumn(name = "recommendation_form_reference", referencedColumnName = "referenceNumber")
    private RecommendationForm recommendationForm; // Foreign key to RecommendationForm entity


}

