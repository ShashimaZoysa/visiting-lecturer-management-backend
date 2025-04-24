package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.RecommendationForm;
import com.visitinglecturer.vlms_backend.entity.VisitingLecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationFormRepository extends JpaRepository<RecommendationForm, Long> {

    // Correct method to find valid recommendation forms
    List<RecommendationForm> findByVisitingLecturerAndAppointmentDateBeforeAndTerminationDateAfter(
            VisitingLecturer visitingLecturer, LocalDate appointmentDate, LocalDate terminationDate);

    RecommendationForm findByReferenceNumber(String referenceNumber);
    //Optional<RecommendationForm> findByReferenceNumber(String referenceNumber);

    // Custom query to find RecommendationForm based on the Visiting Lecturer's NIC
    @Query("SELECT rf FROM RecommendationForm rf " +
            "JOIN rf.visitingLecturer vl " +
            "JOIN vl.user u " +
            "JOIN u.profile p " +
            "WHERE p.nicNumber = :nicNumber")
    List<RecommendationForm> findByVisitingLecturerUserProfileNicNumber(@Param("nicNumber") String nicNumber);

    @Query("SELECT rf FROM RecommendationForm rf " +
            "JOIN rf.visitingLecturer vl " +
            "JOIN vl.user u " +
            "JOIN u.profile p " +
            "JOIN rf.serviceType st " +  // Ensure this matches the field in your entity
            "WHERE p.nicNumber = :nic " +
            "AND st.typeOfService = 'External Online Tutor'")
    List<RecommendationForm> findAllExternalTutorAssignmentsByNic(@Param("nic") String nic);








}


