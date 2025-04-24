package com.visitinglecturer.vlms_backend.controller;

import com.visitinglecturer.vlms_backend.entity.RecommendationForm;
import com.visitinglecturer.vlms_backend.service.RecommendationFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendation-forms")
public class RecommendationFormController {

    @Autowired
    private RecommendationFormService recommendationFormService;

    @PostMapping
    public ResponseEntity<RecommendationForm> createRecommendationForm(@RequestBody RecommendationForm recommendationForm) {
        RecommendationForm savedForm = recommendationFormService.saveRecommendationForm(recommendationForm);
        return ResponseEntity.ok(savedForm);
    }
}

