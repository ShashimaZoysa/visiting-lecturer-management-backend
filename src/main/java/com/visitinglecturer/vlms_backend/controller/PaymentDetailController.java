package com.visitinglecturer.vlms_backend.controller;

import com.visitinglecturer.vlms_backend.entity.VisitingLecturer;
import com.visitinglecturer.vlms_backend.payload.request.PaymentDetailRequest;
import com.visitinglecturer.vlms_backend.payload.response.PaymentDetailResponse;
import com.visitinglecturer.vlms_backend.service.PaymentDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-details")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class PaymentDetailController {

    private final PaymentDetailService paymentDetailService;

    // ✅ Create or Save Payment Detail
    @PostMapping
    public ResponseEntity<PaymentDetailResponse> savePaymentDetail(@RequestBody PaymentDetailRequest paymentDetailRequest) {
        PaymentDetailResponse savedDetail = paymentDetailService.savePaymentDetail(paymentDetailRequest);
        return ResponseEntity.ok(savedDetail);
    }

    // ✅ Get Payment Detail for Authenticated Visiting Lecturer
    @GetMapping
    public ResponseEntity<PaymentDetailResponse> getPaymentDetail() {
        VisitingLecturer lecturer = paymentDetailService.getAuthenticatedVisitingLecturer();
        PaymentDetailResponse paymentDetailResponse = paymentDetailService.getPaymentDetailByVisitingLecturerId(lecturer.getId());
        return ResponseEntity.ok(paymentDetailResponse);
    }

    // ✅ Full Update (PUT) Payment Detail
    @PutMapping
    public ResponseEntity<PaymentDetailResponse> updatePaymentDetail(@RequestBody PaymentDetailRequest paymentDetailRequest) {
        VisitingLecturer lecturer = paymentDetailService.getAuthenticatedVisitingLecturer();
        PaymentDetailResponse updatedDetail = paymentDetailService.updatePaymentDetail(lecturer.getId(), paymentDetailRequest);
        return ResponseEntity.ok(updatedDetail);
    }

    // ✅ Partial Update (PATCH) Payment Detail
    @PatchMapping
    public ResponseEntity<PaymentDetailResponse> patchUpdatePaymentDetail(@RequestBody PaymentDetailRequest paymentDetailRequest) {
        VisitingLecturer lecturer = paymentDetailService.getAuthenticatedVisitingLecturer();
        PaymentDetailResponse updatedDetail = paymentDetailService.patchUpdatePaymentDetail(lecturer.getId(), paymentDetailRequest);
        return ResponseEntity.ok(updatedDetail);
    }
}





