package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkloadType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double workloadHours; // Number of hours for the activity

    private String activityNumber;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // Foreign key to Course entity

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private WorkloadActivity workloadActivity; // Foreign key to WorkloadActivity entity
}

