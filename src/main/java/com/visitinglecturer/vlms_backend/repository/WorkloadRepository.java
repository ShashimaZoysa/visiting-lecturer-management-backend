package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.Workload;
import com.visitinglecturer.vlms_backend.entity.WorkloadGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkloadRepository extends JpaRepository<Workload, Long> {
    // You can add custom queries if needed
    List<Workload> findByWorkloadGroup(WorkloadGroup workloadGroup);
    List<Workload> findByWorkloadGroup_Id(Long groupId);// Define this method

}

