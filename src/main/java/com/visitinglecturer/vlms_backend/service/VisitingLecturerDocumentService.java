package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.entity.*;
import com.visitinglecturer.vlms_backend.repository.DocumentRepository;
import com.visitinglecturer.vlms_backend.repository.DocumentTypeRepository;
import com.visitinglecturer.vlms_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

@Service
public class VisitingLecturerDocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final UserRepository userRepository;

    @Value("${upload.directory}")
    private String uploadDirectory; // Load the upload directory from application.properties

    public VisitingLecturerDocumentService(DocumentRepository documentRepository,
                     DocumentTypeRepository documentTypeRepository,
                     UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.userRepository = userRepository;
    }

    // Method to upload a new documents
    public String uploadDocument(MultipartFile file, Long documentTypeId) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty.");
        }

        // Retrieve the authenticated Visiting Lecturer
        User user = getAuthenticatedVisitingLecturer();

        // Retrieve the DocumentType by ID
        DocumentType documentType = documentTypeRepository.findById(documentTypeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid document type ID"));

        VisitingLecturer visitingLecturer = user.getVisitingLecturer();
        if (visitingLecturer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a Visiting Lecturer");
        }

        // Remove existing document if it exists for the given Visiting Lecturer and DocumentType
        Optional<Document> existingDocument = documentRepository.findByVisitingLecturerAndDocumentType(visitingLecturer, documentType);
        existingDocument.ifPresent(document -> {
            Path oldFilePath = Path.of(uploadDirectory, document.getFileName());
            try {
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            documentRepository.delete(document);
        });

        // Save the new document
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Path.of(uploadDirectory, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Create and save the new Document entity
        Document document = new Document();
        document.setFileName(fileName);
        document.setUploadedBy(user); // Save the user as the uploader
        document.setDocumentType(documentType); // Set the document type
        document.setVisitingLecturer(visitingLecturer); // Associate with the visiting lecturer
        document.setUploadedAt(LocalDateTime.now());

        documentRepository.save(document);

        return "Document uploaded successfully.";
    }


    public String editDocument(MultipartFile file, Long documentTypeId) throws IOException {
        // Retrieve the authenticated Visiting Lecturer
        User user = getAuthenticatedVisitingLecturer();
        VisitingLecturer visitingLecturer = user.getVisitingLecturer();
        if (visitingLecturer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a Visiting Lecturer");
        }

        // Retrieve the DocumentType by ID
        DocumentType documentType = documentTypeRepository.findById(documentTypeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid document type ID"));

        // Find the existing document for the given Visiting Lecturer and Document Type
        Document document = documentRepository.findByVisitingLecturerAndDocumentType(visitingLecturer, documentType)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        // Delete the old file (optional: depends on your logic, if you want to replace it)
        Path existingFilePath = Paths.get(uploadDirectory, document.getFileName());
        File existingFile = existingFilePath.toFile();

        if (existingFile.exists()) {
            // Try deleting the old file
            if (!existingFile.delete()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete existing document file");
            }
        } else {
            // Log that the file wasn't found for deletion (if you're not throwing an error)
            System.out.println("Existing file not found to delete: " + existingFilePath.toString());
        }

        // Save the new file
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Path.of(uploadDirectory, fileName);

        // Ensure the directory exists (create if necessary)
        Files.createDirectories(filePath.getParent());

        // Copy the new file to the desired location
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Update the existing document entity
        document.setFileName(fileName);  // Update the file name
        document.setUploadedAt(LocalDateTime.now());  // Update the timestamp

        // Save the updated document entity back to the repository
        documentRepository.save(document);

        return "Document updated successfully.";
    }

    //Method to check if relevant document is uploaded by the visiting lecturer
    public boolean checkIfDocumentExistsForUser(Long documentTypeId) {
        //Get the authenticated user
        User user = getAuthenticatedVisitingLecturer();
        VisitingLecturer visitingLecturer = user.getVisitingLecturer();

        if (visitingLecturer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a Visiting Lecturer");
        }

        //Get the document type
        DocumentType documentType = documentTypeRepository.findById(documentTypeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid document type ID"));

        //Check if a document already exists for this user and document type
        return documentRepository.findByVisitingLecturerAndDocumentType(visitingLecturer, documentType).isPresent();
    }

    // Method to get the uploaded document for a Visiting Lecturer and specific DocumentType
    public Resource getUploadedDocumentForUser(Long documentTypeId) throws IOException {
        // Retrieve authenticated user
        User user = getAuthenticatedVisitingLecturer();
        VisitingLecturer visitingLecturer = user.getVisitingLecturer();

        if (visitingLecturer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a Visiting Lecturer");
        }

        // Retrieve DocumentType
        DocumentType documentType = documentTypeRepository.findById(documentTypeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid document type ID"));

        // Find the document for the user and document type
        Document document = documentRepository.findByVisitingLecturerAndDocumentType(visitingLecturer, documentType)
                .orElseThrow(() -> new FileNotFoundException("No document found for this user and document type."));

        // Create path to file
        Path filePath = Paths.get(uploadDirectory, document.getFileName());

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found on server: " + filePath.toString());
        }

        return new UrlResource(filePath.toUri());
    }


    // Helper method to get the authenticated Visiting Lecturer
    private User getAuthenticatedVisitingLecturer() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_VISITING_LECTURER")))
                    .orElseThrow(() -> new SecurityException("Only visiting lecturer can upload or edit the upload document."));
        }
        throw new SecurityException("Unauthorized access.");
    }

}
