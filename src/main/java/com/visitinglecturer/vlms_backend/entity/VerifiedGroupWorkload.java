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
public class VerifiedGroupWorkload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "visiting_lecturer_id", nullable = false)
    private VisitingLecturer visitingLecturer; // Foreign key to VisitingLecturer entity

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // Foreign key to Course entity

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private WorkloadGroup group; // Foreign key to WorkloadGroup entity (referred to as 'group')

    private double totalVerifiedHours; // The total verified hours for the lecturer, course, and group

    @Temporal(TemporalType.DATE)
    private Date lastUpdated; // When this total was last updated
}
