package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
    // Spring Data JPA provides this method by default.
    Optional<DocumentType> findById(Long id);
}
