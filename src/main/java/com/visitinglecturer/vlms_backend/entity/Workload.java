package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Workload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private WorkloadGroup workloadGroup; // Foreign key to WorkloadGroup entity


    @ManyToOne
    @JoinColumn(name = "workload_type_id", nullable = false)
    private WorkloadType workloadType; // Foreign key to WorkloadType entity

    @OneToMany(mappedBy = "workload", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<WorkloadVerification> workloadVerifications;

}
