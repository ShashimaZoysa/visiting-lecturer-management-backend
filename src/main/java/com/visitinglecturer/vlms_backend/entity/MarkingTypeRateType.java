package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkingTypeRateType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "marking_type_id", nullable = false)
    private MarkingType markingType;  // Foreign key to MarkingType

    @ManyToOne
    @JoinColumn(name = "marking_rate_type_id", nullable = false)
    private MarkingRateType markingRateType;  // Foreign key to MarkingRateType
}

