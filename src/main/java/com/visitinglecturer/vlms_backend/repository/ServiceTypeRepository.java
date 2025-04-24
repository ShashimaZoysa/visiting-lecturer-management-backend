package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    // Custom queries can be added here
}

