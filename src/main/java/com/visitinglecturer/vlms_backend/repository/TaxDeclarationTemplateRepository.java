package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.TaxDeclarationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxDeclarationTemplateRepository extends JpaRepository<TaxDeclarationTemplate, Long> {

    TaxDeclarationTemplate findTopByOrderByUploadedAtDesc();
}


