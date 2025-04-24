package com.visitinglecturer.vlms_backend.controller;


import com.visitinglecturer.vlms_backend.service.VisitingLecturerDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;


@RestController
@RequestMapping("/api/visiting-lecturer/documents")
@CrossOrigin(origins = "http://localhost:5173")
public class VisitingLecturerDocumentController {

    private final VisitingLecturerDocumentService visitingLecturerDocumentService;

    @Autowired
    public VisitingLecturerDocumentController(VisitingLecturerDocumentService visitingLecturerDocumentService) {
        this.visitingLecturerDocumentService = visitingLecturerDocumentService;
    }

    // Endpoint to upload a new document
    @PostMapping("/upload")
    public String uploadDocument(@RequestParam("file") MultipartFile file,
                                 @RequestParam("documentTypeId") Long documentTypeId) {
        try {
            return visitingLecturerDocumentService.uploadDocument(file, documentTypeId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed due to internal server error");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
        }
    }

    // Endpoint to edit an existing document
    @PutMapping("/edit")
    public String editDocument(@RequestParam("file") MultipartFile file,
                               @RequestParam("documentTypeId") Long documentTypeId) {
        try {
            return visitingLecturerDocumentService.editDocument(file, documentTypeId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File editing failed due to internal server error");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
        }
    }

    // Endpoint to check if a relevant document is uploaded by the visiting lecturer
    @GetMapping("/exist")
    public ResponseEntity<Boolean> checkIfDocumentExistsForUser(@RequestParam("documentTypeId") Long documentTypeId) {
        try {
            boolean documentExists = visitingLecturerDocumentService.checkIfDocumentExistsForUser(documentTypeId);
            return ResponseEntity.ok(documentExists);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An error occurred while checking the document: " + e.getMessage());
        }
    }

    // Endpoint to get the uploaded document for a Visiting Lecturer and specific DocumentType
    @GetMapping("/getDocument")
    public ResponseEntity<Resource> getUploadedDocumentForUser(
            @RequestParam("documentTypeId") Long documentTypeId) {
        try {
            Resource document = visitingLecturerDocumentService.getUploadedDocumentForUser(documentTypeId);

            String contentType = Files.probeContentType(document.getFile().toPath());
            if (contentType == null) {
                contentType = "application/octet-stream"; // fallback
            }

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + document.getFilename() + "\"")
                    .header("Content-Type", contentType)
                    .body(document);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving the document: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An error occurred while fetching the document: " + e.getMessage());
        }
    }




}