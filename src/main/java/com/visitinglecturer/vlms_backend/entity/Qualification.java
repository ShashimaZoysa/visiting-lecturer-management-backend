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
public class Qualification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String level;

    private String university;

    // One qualification can be associated with many visiting lecturers through the join entity
    @OneToMany(mappedBy = "qualification", cascade = CascadeType.ALL)
    private List<QualificationVisitingLecturer> visitingLecturers;

    // Getters and Setters
}


