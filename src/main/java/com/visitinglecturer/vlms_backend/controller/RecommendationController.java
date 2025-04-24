package com.visitinglecturer.vlms_backend.controller;

import com.visitinglecturer.vlms_backend.payload.response.RecommendationResponse;
import com.visitinglecturer.vlms_backend.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendation")
@CrossOrigin(origins = "http://localhost:5173")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{nicNumber}")
    public ResponseEntity<RecommendationResponse> getLecturerDetails(@PathVariable String nicNumber) {
        RecommendationResponse response = recommendationService.getLecturerDetails(nicNumber);
        return ResponseEntity.ok(response);
    }
}
