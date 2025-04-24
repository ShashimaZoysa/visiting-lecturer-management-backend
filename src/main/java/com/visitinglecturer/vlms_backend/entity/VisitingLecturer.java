package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitingLecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String privateAddress;

    private String phoneNumber;

    private String presentEmployment;

    private String department;

    // Getters and Setters
    // One visiting lecturer can have multiple qualifications through the join entity
    @OneToMany(mappedBy = "visitingLecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QualificationVisitingLecturer> qualificationVisitingLecturers;

    @OneToMany(mappedBy = "visitingLecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayeeForm> payeeForms;

    @OneToOne(mappedBy = "visitingLecturer")
    private User user;

}



