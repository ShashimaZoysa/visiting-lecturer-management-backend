package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.PayeeForm;
import com.visitinglecturer.vlms_backend.entity.RecommendationForm;
import com.visitinglecturer.vlms_backend.entity.VisitingLecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayeeFormRepository extends JpaRepository<PayeeForm, Long> {
    // You can add custom queries here later if needed
    Optional<PayeeForm> findByRecommendationFormReferenceNumber(String referenceNumber);

    boolean existsByRecommendationFormAndVisitingLecturer(RecommendationForm recommendationForm, VisitingLecturer visitingLecturer);


}

