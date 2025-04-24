package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.DocumentDueDate;
import com.visitinglecturer.vlms_backend.entity.DocumentType;
import com.visitinglecturer.vlms_backend.entity.VisitingLecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentDueDateRepository extends JpaRepository<DocumentDueDate, Long> {
    Optional<DocumentDueDate> findByVisitingLecturerAndDocumentType(VisitingLecturer lecturer, DocumentType type);
}

