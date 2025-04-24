package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String referenceNumber;

    @ManyToOne
    @JoinColumn(name = "visiting_lecturer_id", nullable = false)
    private VisitingLecturer visitingLecturer;

    @ManyToOne
    @JoinColumn(name = "service_type_id", nullable = false)
    private ServiceType serviceType;  // Now referencing ServiceType

    @ManyToOne
    @JoinColumn(name = "program_of_study_id", nullable = false)
    private ProgramOfStudy programOfStudy;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "centre_id", nullable = false)
    private Centre centre;

    @ManyToOne
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private LocalDate terminationDate;

    private Integer estimatedStudents;

    @Column(nullable = false, length = 20)
    private String medium;

    @Column(nullable = false)
    private Integer workloadHours;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalFinancialCommitment;
}
