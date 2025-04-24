package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.entity.*;
import com.visitinglecturer.vlms_backend.payload.response.AssignedCourseResponse;
import com.visitinglecturer.vlms_backend.payload.response.DocumentExistenceResponse;
import com.visitinglecturer.vlms_backend.payload.response.LecturerProfileResponse;
import com.visitinglecturer.vlms_backend.payload.response.PayeeFormStatusResponse;
import com.visitinglecturer.vlms_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LecturerProfileInfoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VisitingLecturerRepository visitingLecturerRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private RecommendationFormRepository recommendationFormRepository;

    @Autowired
    private PayeeFormRepository payeeFormRepository;

    @Autowired
    private DocumentDueDateRepository documentDueDateRepository;

    @Value("${upload.directory}")
    private String uploadDirectory; // Load the upload directory from application.properties

    public LecturerProfileResponse getProfileByNIC(String nicNumber) {
        User user = userRepository.findByProfile_NicNumber(nicNumber)
                .orElseThrow(() -> new RuntimeException("User with NIC " + nicNumber + " not found"));

        if (user.getProfile() == null) {
            throw new RuntimeException("Profile not found for NIC: " + nicNumber);
        }

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ROLE_ADMIN"));

        if (isAdmin) {
            return new LecturerProfileResponse(
                    user.getProfile().getFullName(),
                    user.getProfile().getGender(),
                    user.getProfile().getEmail(),
                    user.getProfile().getNicNumber(),
                    null,
                    null,
                    "ROLE_ADMIN"
            );
        }

        boolean isLecturer = user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ROLE_VISITING_LECTURER"));

        if (isLecturer && user.getVisitingLecturer() != null) {
            return new LecturerProfileResponse(
                    user.getProfile().getFullName(),
                    user.getProfile().getGender(),
                    user.getProfile().getEmail(),
                    user.getProfile().getNicNumber(),
                    user.getVisitingLecturer().getPhoneNumber(),
                    user.getVisitingLecturer().getPrivateAddress(),
                    "ROLE_VISITING_LECTURER"
            );
        }

        throw new RuntimeException("No matching role data found for NIC: " + nicNumber);
    }

    public LecturerProfileResponse updateProfileByNIC(String nicNumber, LecturerProfileResponse updatedProfile) {
        User user = userRepository.findByProfile_NicNumber(nicNumber)
                .orElseThrow(() -> new RuntimeException("User with NIC " + nicNumber + " not found"));

        // Update profile fields
        if (user.getProfile() != null) {
            user.getProfile().setFullName(updatedProfile.getFullName());
            user.getProfile().setGender(updatedProfile.getGender());
            user.getProfile().setEmail(updatedProfile.getEmail());
            user.getProfile().setNicNumber(updatedProfile.getNicNumber()); // Be cautious if NIC is linked to login
        }

        String role = null;

        // If user is a visiting lecturer, update their details too
        if (user.getRoles().stream().anyMatch(roleObj -> roleObj.getName().equalsIgnoreCase("ROLE_VISITING_LECTURER"))) {
            role = "ROLE_VISITING_LECTURER";
            if (user.getVisitingLecturer() != null) {
                user.getVisitingLecturer().setPhoneNumber(updatedProfile.getPhoneNumber());
                user.getVisitingLecturer().setPrivateAddress(updatedProfile.getPrivateAddress());
            }
        } else if (user.getRoles().stream().anyMatch(roleObj -> roleObj.getName().equalsIgnoreCase("ROLE_ADMIN"))) {
            role = "ROLE_ADMIN";
        }

        userRepository.save(user); // Persist changes

        return new LecturerProfileResponse(
                user.getProfile().getFullName(),
                user.getProfile().getGender(),
                user.getProfile().getEmail(),
                user.getProfile().getNicNumber(),
                user.getVisitingLecturer() != null ? user.getVisitingLecturer().getPhoneNumber() : null,
                user.getVisitingLecturer() != null ? user.getVisitingLecturer().getPrivateAddress() : null,
                role
        );
    }
    // Check if a document is uploaded for a specific Visiting Lecturer (by NIC or entity)
    public boolean checkIfDocumentExistsForLecturer(String nic, Long documentTypeId) {
        // Retrieve the user by the NIC number in the Profile entity
        User user = userRepository.findByProfile_NicNumber(nic)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid NIC or no lecturer found"));

        VisitingLecturer lecturer = user.getVisitingLecturer();

        DocumentType documentType = documentTypeRepository.findById(documentTypeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid document type ID"));

        return documentRepository.findByVisitingLecturerAndDocumentType(lecturer, documentType).isPresent();
    }

    public Resource getDocumentResourceByNicAndType(String nic, Long documentTypeId) throws IOException {
        // 1. Find the user by NIC
        User user = userRepository.findByProfile_NicNumber(nic)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid NIC or no lecturer found"));

        VisitingLecturer lecturer = user.getVisitingLecturer();

        if (lecturer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a Visiting Lecturer");
        }

        // 2. Find the DocumentType
        DocumentType documentType = documentTypeRepository.findById(documentTypeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid document type ID"));

        // 3. Find the document
        Document document = documentRepository.findByVisitingLecturerAndDocumentType(lecturer, documentType)
                .orElseThrow(() -> new FileNotFoundException("Document not found for this NIC and document type."));

        // 4. Locate file on disk
        Path filePath = Paths.get(uploadDirectory, document.getFileName());

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found on server: " + filePath.toString());
        }

        // 5. Return as Resource
        return new UrlResource(filePath.toUri());
    }

    // Method to fetch assigned courses for a lecturer based on NIC
    public List<AssignedCourseResponse> getAssignedCoursesByLecturerNIC(String nic) {
        // Fetching the RecommendationForm records by Visiting Lecturer's NIC
        List<RecommendationForm> recommendationForms = recommendationFormRepository.findByVisitingLecturerUserProfileNicNumber(nic);

        // Mapping RecommendationForm to AssignedCourseResponse
        return recommendationForms.stream()
                .map(form -> new AssignedCourseResponse(
                        form.getCourse().getCourseCode(),
                        form.getCourse().getCourseTitle(),
                        form.getReferenceNumber(),
                        form.getServiceType().getTypeOfService(),
                        form.getAcademicYear().getAcademicYear()
                ))
                .collect(Collectors.toList());
    }

    public List<PayeeFormStatusResponse> getPayeeFormStatusByNic(String nicNumber) {
        // Fetch RecommendationForms for the given NIC using the custom query
        List<RecommendationForm> recommendationForms = recommendationFormRepository.findAllExternalTutorAssignmentsByNic(nicNumber);

        // Sort by the latest academic year (assuming the format "2023/2024")
        recommendationForms.sort((a, b) -> b.getAcademicYear().getAcademicYear().compareTo(a.getAcademicYear().getAcademicYear()));

        // Create the response list to be returned
        List<PayeeFormStatusResponse> responseList = new ArrayList<>();

        // Iterate over the sorted RecommendationForms
        for (RecommendationForm form : recommendationForms) {
            // Check if the payee form is completed for this RecommendationForm
            boolean payeeFormCompleted = payeeFormRepository.existsByRecommendationFormAndVisitingLecturer(form, form.getVisitingLecturer());

            // Always show reference number from the RecommendationForm
            String referenceNumber = form.getReferenceNumber();

            // Add the result to the response list
            responseList.add(new PayeeFormStatusResponse(
                    form.getCourse().getCourseCode(),
                    form.getCourse().getCourseTitle(),
                    form.getAcademicYear().getAcademicYear(),
                    form.getServiceType().getTypeOfService(),
                    payeeFormCompleted,
                    referenceNumber
            ));
        }

        return responseList;
    }

    public DocumentDueDate saveDocumentDueDate(Long visitingLecturerId, Long documentTypeId, LocalDate dueDate) {
        VisitingLecturer visitingLecturer = visitingLecturerRepository.findById(visitingLecturerId)
                .orElseThrow(() -> new RuntimeException("Visiting Lecturer not found"));

        DocumentType documentType = documentTypeRepository.findById(documentTypeId)
                .orElseThrow(() -> new RuntimeException("Document Type not found"));

        // Check if the document is already uploaded
        boolean alreadyUploaded = documentRepository
                .findByVisitingLecturerAndDocumentType(visitingLecturer, documentType)
                .isPresent();

        if (alreadyUploaded) {
            throw new RuntimeException("Document already uploaded — cannot assign a due date");
        }

        // Check if a due date already exists
        Optional<DocumentDueDate> existing = documentDueDateRepository
                .findByVisitingLecturerAndDocumentType(visitingLecturer, documentType);

        if (existing.isPresent()) {
            throw new RuntimeException("Due date already exists for this document and lecturer");
        }

        // Save due date
        DocumentDueDate dueDateEntry = new DocumentDueDate();
        dueDateEntry.setVisitingLecturer(visitingLecturer);
        dueDateEntry.setDocumentType(documentType);
        dueDateEntry.setDueDate(dueDate); // Can be null if needed

        return documentDueDateRepository.save(dueDateEntry);
    }

    public DocumentExistenceResponse checkDocumentExistenceAndUploadDateForLecturer(String nic, Long documentTypeId) {
        // Retrieve the user by the NIC number in the Profile entity
        User user = userRepository.findByProfile_NicNumber(nic)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid NIC or no lecturer found"));

        VisitingLecturer lecturer = user.getVisitingLecturer();

        // Retrieve the document type by its ID
        DocumentType documentType = documentTypeRepository.findById(documentTypeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid document type ID"));

        // Check if the document exists for the given lecturer and document type
        Optional<Document> documentOptional = documentRepository.findByVisitingLecturerAndDocumentType(lecturer, documentType);

        // Prepare response
        DocumentExistenceResponse response = new DocumentExistenceResponse();

        // Set the Visiting Lecturer ID (fetched from the lecturer)
        response.setVisitingLecturerId(lecturer.getId());

        if (documentOptional.isPresent()) {
            Document document = documentOptional.get();
            response.setDocumentExists(true);
            response.setUploadDate(document.getUploadedAt().toLocalDate());    // Set upload date in the response
        } else {
            response.setDocumentExists(false);
        }

        return response;
    }







}


