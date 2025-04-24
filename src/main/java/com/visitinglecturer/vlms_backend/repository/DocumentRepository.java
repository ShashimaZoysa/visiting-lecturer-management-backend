package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.Document;
import com.visitinglecturer.vlms_backend.entity.DocumentType;
import com.visitinglecturer.vlms_backend.entity.User;
import com.visitinglecturer.vlms_backend.entity.VisitingLecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    //Document findByVisitingLecturerAndDocumentType(VisitingLecturer lecturer, DocumentType documentType);

    // Custom query to find a Document by User and DocumentType
    Optional<Document> findByVisitingLecturerAndDocumentType(VisitingLecturer visitingLecturer, DocumentType documentType);


}

