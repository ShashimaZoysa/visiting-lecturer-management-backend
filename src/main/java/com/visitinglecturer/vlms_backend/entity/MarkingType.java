package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long markingTypeId;

    @Column(nullable = false)
    private String typeName;  // e.g., "TMA", "Online Delivery"
}

