package com.visitinglecturer.vlms_backend.controller;

import com.visitinglecturer.vlms_backend.payload.response.VisitingLecturerDetailsResponse;
import com.visitinglecturer.vlms_backend.service.VisitingLecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visiting-lecturers")
@CrossOrigin(origins = "http://localhost:5173")
public class VisitingLecturerController {

    private final VisitingLecturerService visitingLecturerService;

    @Autowired
    public VisitingLecturerController(VisitingLecturerService visitingLecturerService) {
        this.visitingLecturerService = visitingLecturerService;
    }

    @GetMapping("/{nicNumber}")
    public ResponseEntity<VisitingLecturerDetailsResponse> getVisitingLecturerDetails(@PathVariable String nicNumber) {
        return ResponseEntity.ok(visitingLecturerService.getVisitingLecturerDetails(nicNumber));
    }
}

