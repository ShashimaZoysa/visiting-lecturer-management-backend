package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayeeWorkloadActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payee_form_id", nullable = false)
    private PayeeForm payeeForm;

    @ManyToOne
    @JoinColumn(name = "workload_activity_id", nullable = false)
    private WorkloadActivity workloadActivity;
}

