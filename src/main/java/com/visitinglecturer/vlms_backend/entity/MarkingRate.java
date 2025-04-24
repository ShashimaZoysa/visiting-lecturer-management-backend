package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkingRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "marking_type_id", nullable = false)
    private MarkingType markingType;  // Foreign key to MarkingType

    @ManyToOne
    @JoinColumn(name = "marking_activity_id", nullable = false)
    private MarkingActivity markingActivity;  // Foreign key to MarkingActivity

    @ManyToOne
    @JoinColumn(name = "marking_rate_type_id", nullable = false)
    private MarkingRateType markingRateType;  // Foreign key to MarkingRateType

    @Column(nullable = false)
    private Double rate;  // The actual rate value (e.g., per script, per hour, etc.)
}

