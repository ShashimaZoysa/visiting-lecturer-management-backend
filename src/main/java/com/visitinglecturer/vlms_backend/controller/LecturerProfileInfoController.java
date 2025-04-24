package com.visitinglecturer.vlms_backend.controller;

import com.visitinglecturer.vlms_backend.entity.DocumentDueDate;
import com.visitinglecturer.vlms_backend.payload.request.DocumentDueDateRequest;
import com.visitinglecturer.vlms_backend.payload.response.AssignedCourseResponse;
import com.visitinglecturer.vlms_backend.payload.response.DocumentExistenceResponse;
import com.visitinglecturer.vlms_backend.payload.response.LecturerProfileResponse;
import com.visitinglecturer.vlms_backend.payload.response.PayeeFormStatusResponse;
import com.visitinglecturer.vlms_backend.service.LecturerProfileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:5173")
public class LecturerProfileInfoController {

    @Autowired
    private LecturerProfileInfoService lecturerProfileInfoService;

    @GetMapping("/{nicNumber}")
    public ResponseEntity<LecturerProfileResponse> getLecturerProfileByNIC(@PathVariable String nicNumber) {
        LecturerProfileResponse profile = lecturerProfileInfoService.getProfileByNIC(nicNumber);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{nicNumber}")
    public LecturerProfileResponse updateProfileByNIC(
            @PathVariable String nicNumber,
            @RequestBody LecturerProfileResponse updatedProfile
    ) {
        return lecturerProfileInfoService.updateProfileByNIC(nicNumber, updatedProfile);
    }

    @GetMapping("/document/check-existence")
    public ResponseEntity<Boolean> checkIfDocumentExists(
            @RequestParam String nic,
            @RequestParam Long documentTypeId) {
        try {
            boolean documentExists = lecturerProfileInfoService.checkIfDocumentExistsForLecturer(nic, documentTypeId);
            return new ResponseEntity<>(documentExists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/document/download")
    public ResponseEntity<Resource> downloadDocumentByNicAndType(
            @RequestParam String nic,
            @RequestParam Long documentTypeId,
            @RequestParam(required = false, defaultValue = "download") String mode) {  // mode defaults to "download"
        try {
            // Get the document
            Resource document = lecturerProfileInfoService.getDocumentResourceByNicAndType(nic, documentTypeId);

            // Detect content type
            String contentType = Files.probeContentType(document.getFile().toPath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // Prepare headers based on the mode
            String contentDisposition;
            if ("view".equalsIgnoreCase(mode)) {
                contentDisposition = "inline; filename=\"" + document.getFilename() + "\"";  // For viewing in the browser
            } else {
                contentDisposition = "attachment; filename=\"" + document.getFilename() + "\"";  // For downloading
            }

            return ResponseEntity.ok()
                    .header("Content-Disposition", contentDisposition)
                    .header("Content-Type", contentType)
                    .body(document);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving the document: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/assigned-courses")
    public List<AssignedCourseResponse> getAssignedCoursesByLecturerNIC(@RequestParam String nic) {
        return lecturerProfileInfoService.getAssignedCoursesByLecturerNIC(nic);
    }

    @GetMapping("/status/{nicNumber}")
    public List<PayeeFormStatusResponse> getPayeeFormStatusByNic(@PathVariable String nicNumber) {
        return lecturerProfileInfoService.getPayeeFormStatusByNic(nicNumber);
    }


    @PostMapping("/due-date")
    public ResponseEntity<?> saveDocumentDueDate(
            @RequestParam Long visitingLecturerId,
            @RequestParam Long documentTypeId,
            @RequestParam LocalDate dueDate) {
        try {
            // Call the service method to save the due date
            DocumentDueDate savedDueDate = lecturerProfileInfoService.saveDocumentDueDate(visitingLecturerId, documentTypeId, dueDate);

            // Return the saved due date with a successful status
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDueDate);

        } catch (RuntimeException e) {
            // Handle any runtime exceptions like duplicate or already uploaded
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/document/check-existence-with-date")
    public ResponseEntity<DocumentExistenceResponse> checkDocumentExistenceAndUploadDate(
            @RequestParam String nic,
            @RequestParam Long documentTypeId) {  // Removed the lecturerId parameter
        try {
            // Call the service to check document existence and retrieve the upload date
            DocumentExistenceResponse response = lecturerProfileInfoService.checkDocumentExistenceAndUploadDateForLecturer(nic, documentTypeId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // If any error occurs, return a BAD_REQUEST status
            return new ResponseEntity<>(new DocumentExistenceResponse(false, null, null), HttpStatus.BAD_REQUEST);
        }
    }







}
