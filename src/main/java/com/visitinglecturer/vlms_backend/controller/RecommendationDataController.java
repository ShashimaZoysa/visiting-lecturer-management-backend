package com.visitinglecturer.vlms_backend.controller;

import com.visitinglecturer.vlms_backend.entity.*;
import com.visitinglecturer.vlms_backend.service.RecommendationDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation-data")
@CrossOrigin(origins = "http://localhost:5173")
public class RecommendationDataController {

    private final RecommendationDataService recommendationDataService;

    @Autowired
    public RecommendationDataController(RecommendationDataService recommendationDataService) {
        this.recommendationDataService = recommendationDataService;
    }

    // Endpoint to get all programs
    @GetMapping("/programs")
    public List<ProgramOfStudy> getAllPrograms() {
        return recommendationDataService.getAllPrograms();
    }

    // Endpoint to get all courses
    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return recommendationDataService.getAllCourses();
    }

    // Endpoint to get all centres
    @GetMapping("/centres")
    public List<Centre> getAllCentres() {
        return recommendationDataService.getAllCentres();
    }

    // Endpoint to get all academic years
    @GetMapping("/academic-years")
    public List<AcademicYear> getAllAcademicYears() {
        return recommendationDataService.getAllAcademicYears();
    }

    // Endpoint to get all service types
    @GetMapping("/service-types")
    public List<ServiceType> getAllServiceTypes() {
        return recommendationDataService.getAllServiceTypes();
    }
}

