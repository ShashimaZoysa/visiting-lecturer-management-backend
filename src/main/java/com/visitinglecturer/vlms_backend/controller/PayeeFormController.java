package com.visitinglecturer.vlms_backend.controller;

import com.visitinglecturer.vlms_backend.payload.request.PayeeFormRequest;
import com.visitinglecturer.vlms_backend.entity.WorkloadActivity;
import com.visitinglecturer.vlms_backend.payload.response.PayeeFormResponse;
import com.visitinglecturer.vlms_backend.repository.WorkloadActivityRepository;
import com.visitinglecturer.vlms_backend.service.PayeeFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/payee-form")
@CrossOrigin(origins = "http://localhost:5173")
public class PayeeFormController {

    private final PayeeFormService payeeFormService;

    @Autowired
    public PayeeFormController(PayeeFormService payeeFormService) {
        this.payeeFormService = payeeFormService;
    }

    /**
     * Endpoint to fetch and prepare a payee form by reference number.
     * @param referenceNumber The reference number associated with the recommendation form.
     * @return Prepared PayeeFormResponse object.
     */
    @GetMapping("/prepare/{referenceNumber}")
    public ResponseEntity<PayeeFormResponse> preparePayeeForm(@PathVariable String referenceNumber) {
        try {
            PayeeFormResponse payeeFormResponse = payeeFormService.preparePayeeFormByReferenceNumber(referenceNumber);
            return ResponseEntity.ok(payeeFormResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Endpoint to submit the full PayeeForm once the user has filled it.
     * @param referenceNumber The reference number associated with the recommendation form.
     * @param request The PayeeFormRequest object to be submitted.
     * @param signatureFile The digital signature file to be uploaded.
     * @return Saved PayeeFormResponse object.
     */
    @PostMapping("/submit")
    public ResponseEntity<PayeeFormResponse> submitPayeeForm(
            @RequestParam("referenceNumber") String referenceNumber,
            @ModelAttribute PayeeFormRequest request, // Using PayeeFormRequest
            @RequestParam(value = "signatureFile", required = false) MultipartFile signatureFile) {
        try {
            // Pass referenceNumber, PayeeFormRequest, and signatureFile to the service
            PayeeFormResponse savedFormResponse = payeeFormService.submitPayeeForm(referenceNumber, request, signatureFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFormResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Autowired
    private WorkloadActivityRepository workloadActivityRepository;
    @GetMapping("/workload-activities")
    public List<WorkloadActivity> getAllWorkloadActivities() {
        return workloadActivityRepository.findAll();
    }
}




