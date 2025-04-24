package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramOfStudy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String programName;  // Abbreviations like "BSE"

    private String fullProgramName;  // Optional full name like "Bachelor of Software Engineering"
}

