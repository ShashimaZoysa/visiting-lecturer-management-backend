package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {

    // You can add custom queries here if necessary
    // For example, find by VisitingLecturer
    Optional<PaymentDetail> findByVisitingLecturerId(Long visitingLecturerId);

}
