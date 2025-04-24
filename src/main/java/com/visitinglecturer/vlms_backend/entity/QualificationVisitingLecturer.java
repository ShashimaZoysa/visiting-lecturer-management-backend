package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualificationVisitingLecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "visiting_lecturer_id", nullable = false)
    private VisitingLecturer visitingLecturer;

    @ManyToOne
    @JoinColumn(name = "qualification_id", nullable = false)
    private Qualification qualification;

}




