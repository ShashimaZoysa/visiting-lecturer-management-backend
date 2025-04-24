package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visiting_lecturer_id", nullable = false)
    private VisitingLecturer visitingLecturer;

    @Column(nullable = false)
    private String nicFullName;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String branchName;

    @Column(nullable = false)
    private String branchCode;

    @Column(nullable = false)
    private String accountNumber;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

}

