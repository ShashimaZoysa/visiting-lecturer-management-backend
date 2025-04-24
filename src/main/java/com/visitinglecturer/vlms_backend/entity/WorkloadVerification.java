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
public class WorkloadVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date verificationDate; // Date of verification

    @ManyToOne
    @JoinColumn(name = "workload_id", nullable = false)
    private Workload workload; // Foreign key to Workload entity

    @ManyToOne
    @JoinColumn(name = "verification_status_id", nullable = false)
    private WorkloadVerificationStatus verificationStatus; // Foreign key to WorkloadVerificationStatus entity

    @ManyToOne
    @JoinColumn(name = "verified_by_user_id")
    private User verifiedByUser; // Foreign key to User entity (Nullable if not verified)
}

