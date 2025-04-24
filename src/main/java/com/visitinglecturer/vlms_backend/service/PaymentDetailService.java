package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.entity.PaymentDetail;
import com.visitinglecturer.vlms_backend.entity.User;
import com.visitinglecturer.vlms_backend.entity.VisitingLecturer;
import com.visitinglecturer.vlms_backend.payload.request.PaymentDetailRequest;
import com.visitinglecturer.vlms_backend.payload.response.PaymentDetailResponse;
import com.visitinglecturer.vlms_backend.repository.PaymentDetailRepository;
import com.visitinglecturer.vlms_backend.repository.UserRepository;
import com.visitinglecturer.vlms_backend.util.EncryptionUtil;  // Import EncryptionUtil
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentDetailService {

    private final PaymentDetailRepository paymentDetailRepository;
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;  // Add EncryptionUtil dependency

    // Save payment detail after converting PaymentDetailRequest to PaymentDetail entity
    public PaymentDetailResponse savePaymentDetail(PaymentDetailRequest paymentDetailRequest) {
        VisitingLecturer visitingLecturer = getAuthenticatedVisitingLecturer();

        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setVisitingLecturer(visitingLecturer);
        paymentDetail.setNicFullName(paymentDetailRequest.getNicFullName());
        paymentDetail.setBankName(paymentDetailRequest.getBankName());
        paymentDetail.setBranchName(paymentDetailRequest.getBranchName());
        paymentDetail.setBranchCode(paymentDetailRequest.getBranchCode());

        // Encrypt the account number before saving
        String encryptedAccountNumber = encryptionUtil.encrypt(paymentDetailRequest.getAccountNumber());
        paymentDetail.setAccountNumber(encryptedAccountNumber);

        PaymentDetail savedPaymentDetail = paymentDetailRepository.save(paymentDetail);

        return mapToPaymentDetailResponse(savedPaymentDetail);
    }

    // Get PaymentDetail by VisitingLecturerId
    public PaymentDetailResponse getPaymentDetailByVisitingLecturerId(Long visitingLecturerId) {
        PaymentDetail paymentDetail = paymentDetailRepository.findByVisitingLecturerId(visitingLecturerId)
                .orElseThrow(() -> new RuntimeException("Payment details not found for lecturer ID: " + visitingLecturerId));

        // Decrypt the account number before returning it
        String decryptedAccountNumber = encryptionUtil.decrypt(paymentDetail.getAccountNumber());

        return mapToPaymentDetailResponse(paymentDetail, decryptedAccountNumber);
    }

    // PUT: Update all payment details
    public PaymentDetailResponse updatePaymentDetail(Long visitingLecturerId, PaymentDetailRequest paymentDetailRequest) {
        PaymentDetail existingDetail = paymentDetailRepository.findByVisitingLecturerId(visitingLecturerId)
                .orElseThrow(() -> new RuntimeException("Payment details not found for lecturer ID: " + visitingLecturerId));

        existingDetail.setNicFullName(paymentDetailRequest.getNicFullName());
        existingDetail.setBankName(paymentDetailRequest.getBankName());
        existingDetail.setBranchName(paymentDetailRequest.getBranchName());
        existingDetail.setBranchCode(paymentDetailRequest.getBranchCode());

        // Encrypt the account number before saving
        String encryptedAccountNumber = encryptionUtil.encrypt(paymentDetailRequest.getAccountNumber());
        existingDetail.setAccountNumber(encryptedAccountNumber);

        PaymentDetail updatedPaymentDetail = paymentDetailRepository.save(existingDetail);
        return mapToPaymentDetailResponse(updatedPaymentDetail);
    }

    // PATCH: Partially update payment details
    public PaymentDetailResponse patchUpdatePaymentDetail(Long visitingLecturerId, PaymentDetailRequest paymentDetailRequest) {
        PaymentDetail existingDetail = paymentDetailRepository.findByVisitingLecturerId(visitingLecturerId)
                .orElseThrow(() -> new RuntimeException("Payment details not found for lecturer ID: " + visitingLecturerId));

        if (paymentDetailRequest.getNicFullName() != null) {
            existingDetail.setNicFullName(paymentDetailRequest.getNicFullName());
        }
        if (paymentDetailRequest.getBankName() != null) {
            existingDetail.setBankName(paymentDetailRequest.getBankName());
        }
        if (paymentDetailRequest.getBranchName() != null) {
            existingDetail.setBranchName(paymentDetailRequest.getBranchName());
        }
        if (paymentDetailRequest.getBranchCode() != null) {
            existingDetail.setBranchCode(paymentDetailRequest.getBranchCode());
        }
        if (paymentDetailRequest.getAccountNumber() != null) {
            // Encrypt the account number before saving
            String encryptedAccountNumber = encryptionUtil.encrypt(paymentDetailRequest.getAccountNumber());
            existingDetail.setAccountNumber(encryptedAccountNumber);
        }

        PaymentDetail updatedPaymentDetail = paymentDetailRepository.save(existingDetail);
        return mapToPaymentDetailResponse(updatedPaymentDetail);
    }

    // Helper method to check if payment detail exists by Visiting Lecturer ID
    public boolean existsByVisitingLecturerId(Long visitingLecturerId) {
        return paymentDetailRepository.findByVisitingLecturerId(visitingLecturerId).isPresent();
    }

    // Helper method to get the authenticated Visiting Lecturer
    public VisitingLecturer getAuthenticatedVisitingLecturer() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .filter(user -> user.getRoles().stream()
                            .anyMatch(role -> role.getName().equals("ROLE_VISITING_LECTURER")))
                    .map(User::getVisitingLecturer)
                    .orElseThrow(() -> new SecurityException("Only visiting lecturer can perform this action."));
        }
        throw new SecurityException("Unauthorized access.");
    }

    // Map PaymentDetail entity to PaymentDetailResponse DTO
    private PaymentDetailResponse mapToPaymentDetailResponse(PaymentDetail paymentDetail) {
        // Decrypt the account number before mapping
        String decryptedAccountNumber = encryptionUtil.decrypt(paymentDetail.getAccountNumber());
        return new PaymentDetailResponse(
                paymentDetail.getNicFullName(),
                paymentDetail.getBankName(),
                paymentDetail.getBranchName(),
                paymentDetail.getBranchCode(),
                decryptedAccountNumber
        );
    }

    // Overloaded method to handle decrypted account number
    private PaymentDetailResponse mapToPaymentDetailResponse(PaymentDetail paymentDetail, String decryptedAccountNumber) {
        return new PaymentDetailResponse(
                paymentDetail.getNicFullName(),
                paymentDetail.getBankName(),
                paymentDetail.getBranchName(),
                paymentDetail.getBranchCode(),
                decryptedAccountNumber
        );
    }
}





