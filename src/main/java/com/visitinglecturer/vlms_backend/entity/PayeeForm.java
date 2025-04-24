package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayeeForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String officialAddress;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recommendation_form_id", nullable = false)
    private RecommendationForm recommendationForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visiting_lecturer_id", nullable = false)
    private VisitingLecturer visitingLecturer;

    @OneToMany(mappedBy = "payeeForm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayeeWorkloadActivity> payeeWorkloadActivities;

    @Column(nullable = false)
    private Integer totalHours;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalFeeClaimed;

    @Column(nullable = false)
    private String digitalSignatureFileName;

    // New field to track submission date
    @Column(nullable = true) // Nullable in case the form is not submitted yet
    private LocalDate submittedAt;

    // Transient Getters for Derived Data
    @Transient
    public String getReferenceNumber() {
        return recommendationForm != null ? recommendationForm.getReferenceNumber() : null;
    }

    @Transient
    public LocalDate getAppointmentDate() {
        return recommendationForm != null ? recommendationForm.getAppointmentDate() : null;
    }

    @Transient
    public String getCourseCode() {
        return recommendationForm != null && recommendationForm.getCourse() != null
                ? recommendationForm.getCourse().getCourseCode()
                : null;
    }

    @Transient
    public String getProgramName() {
        return recommendationForm != null && recommendationForm.getProgramOfStudy() != null
                ? recommendationForm.getProgramOfStudy().getProgramName()
                : null;
    }

    @Transient
    public String getPrivateAddress() {
        return recommendationForm != null && recommendationForm.getVisitingLecturer() != null
                ? recommendationForm.getVisitingLecturer().getPrivateAddress()
                : null;
    }

    // Transient Getter for Full Name from User's Profile
    @Transient
    public String getFullName() {
        return visitingLecturer != null && visitingLecturer.getUser() != null && visitingLecturer.getUser().getProfile() != null
                ? visitingLecturer.getUser().getProfile().getFullName()
                : null;
    }
}



