package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.payload.request.PayeeFormRequest;
import com.visitinglecturer.vlms_backend.entity.*;
import com.visitinglecturer.vlms_backend.payload.response.PayeeFormResponse;
import com.visitinglecturer.vlms_backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PayeeFormService {

    private final PayeeFormRepository payeeFormRepository;
    private final RecommendationFormRepository recommendationFormRepository;
    private final VisitingLecturerRepository visitingLecturerRepository;
    private final WorkloadActivityRepository workloadActivityRepository;
    private final PayeeWorkloadActivityRepository payeeWorkloadActivityRepository;

    @Value("${signature.upload.directory}")
    private String signatureUploadDir;

    public PayeeFormService(
            PayeeFormRepository payeeFormRepository,
            RecommendationFormRepository recommendationFormRepository,
            VisitingLecturerRepository visitingLecturerRepository,
            WorkloadActivityRepository workloadActivityRepository,
            PayeeWorkloadActivityRepository payeeWorkloadActivityRepository
    ) {
        this.payeeFormRepository = payeeFormRepository;
        this.recommendationFormRepository = recommendationFormRepository;
        this.visitingLecturerRepository = visitingLecturerRepository;
        this.workloadActivityRepository = workloadActivityRepository;
        this.payeeWorkloadActivityRepository = payeeWorkloadActivityRepository;
    }

    public PayeeFormResponse preparePayeeFormByReferenceNumber(String referenceNumber) {
        RecommendationForm recommendationForm = recommendationFormRepository.findByReferenceNumber(referenceNumber);
        if (recommendationForm == null) {
            throw new RuntimeException("Recommendation form not found for reference: " + referenceNumber);
        }

        Optional<PayeeForm> existingForm = payeeFormRepository.findByRecommendationFormReferenceNumber(referenceNumber);

        PayeeFormResponse response = new PayeeFormResponse();
        response.setReferenceNumber(recommendationForm.getReferenceNumber());
        response.setAppointmentDate(recommendationForm.getAppointmentDate());
        response.setCourseCode(recommendationForm.getCourse() != null ? recommendationForm.getCourse().getCourseCode() : null);
        response.setProgramName(recommendationForm.getProgramOfStudy() != null ? recommendationForm.getProgramOfStudy().getProgramName() : null);
        response.setPrivateAddress(recommendationForm.getVisitingLecturer() != null ? recommendationForm.getVisitingLecturer().getPrivateAddress() : null);
        response.setFullName(recommendationForm.getVisitingLecturer() != null && recommendationForm.getVisitingLecturer().getUser() != null
                ? recommendationForm.getVisitingLecturer().getUser().getProfile().getFullName() : null);

        response.setAlreadySubmitted(existingForm.isPresent());

        return response;
    }

    @Transactional
    public PayeeFormResponse submitPayeeForm(String referenceNumber, PayeeFormRequest request, MultipartFile signatureFile) {
        RecommendationForm recommendation = recommendationFormRepository.findByReferenceNumber(referenceNumber);
        if (recommendation == null) {
            throw new RuntimeException("Recommendation form not found for reference number: " + referenceNumber);
        }

        Optional<PayeeForm> existingForm = payeeFormRepository.findByRecommendationFormReferenceNumber(referenceNumber);
        if (existingForm.isPresent()) {
            throw new RuntimeException("A Payee Form has already been submitted for this reference number.");
        }

        VisitingLecturer lecturer = recommendation.getVisitingLecturer();

        PayeeForm form = new PayeeForm();
        form.setRecommendationForm(recommendation);
        form.setVisitingLecturer(lecturer);
        form.setOfficialAddress(request.getOfficialAddress());
        form.setTotalHours(request.getTotalHours());
        form.setTotalFeeClaimed(request.getTotalFeeClaimed() != null ? request.getTotalFeeClaimed() : BigDecimal.ZERO);

        // Handle digital signature upload
        if (signatureFile != null && !signatureFile.isEmpty()) {
            String fileName = saveSignatureFile(signatureFile);
            form.setDigitalSignatureFileName(fileName);
        }

        form.setSubmittedAt(LocalDate.now()); // Add this line to track the submission date

        // Save PayeeForm first
        PayeeForm savedForm = payeeFormRepository.save(form);

        // Process workload activities
        List<PayeeWorkloadActivity> payeeWorkloadActivities = new ArrayList<>();
        for (Long activityId : request.getWorkloadActivityIds()) {
            WorkloadActivity activity = workloadActivityRepository.findById(activityId)
                    .orElseThrow(() -> new RuntimeException("WorkloadActivity not found with ID: " + activityId));

            PayeeWorkloadActivity payeeWorkloadActivity = new PayeeWorkloadActivity();
            payeeWorkloadActivity.setPayeeForm(savedForm);
            payeeWorkloadActivity.setWorkloadActivity(activity);
            payeeWorkloadActivities.add(payeeWorkloadActivity);
        }

        payeeWorkloadActivityRepository.saveAll(payeeWorkloadActivities);

        return convertToPayeeFormResponse(savedForm);
    }

    private String saveSignatureFile(MultipartFile file) {
        try {
            File dir = new File(signatureUploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalFilename;
            Path filePath = Paths.get(signatureUploadDir, fileName);
            Files.write(filePath, file.getBytes());
            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to save digital signature file", e);
        }
    }

    private PayeeFormResponse convertToPayeeFormResponse(PayeeForm payeeForm) {
        RecommendationForm recommendationForm = payeeForm.getRecommendationForm();

        PayeeFormResponse response = new PayeeFormResponse();
        response.setReferenceNumber(recommendationForm.getReferenceNumber());
        response.setAppointmentDate(recommendationForm.getAppointmentDate());
        response.setCourseCode(recommendationForm.getCourse() != null ? recommendationForm.getCourse().getCourseCode() : null);
        response.setProgramName(recommendationForm.getProgramOfStudy() != null ? recommendationForm.getProgramOfStudy().getProgramName() : null);
        response.setPrivateAddress(recommendationForm.getVisitingLecturer() != null ? recommendationForm.getVisitingLecturer().getPrivateAddress() : null);
        response.setFullName(recommendationForm.getVisitingLecturer() != null && recommendationForm.getVisitingLecturer().getUser() != null
                ? recommendationForm.getVisitingLecturer().getUser().getProfile().getFullName() : null);

        return response;
    }
}



