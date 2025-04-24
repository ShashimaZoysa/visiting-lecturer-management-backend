package com.visitinglecturer.vlms_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visitinglecturer.vlms_backend.entity.MarkingRate;
import com.visitinglecturer.vlms_backend.service.MarkingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marking-rates")
@CrossOrigin(origins = "http://localhost:5173")
public class MarkingRateController {

    @Autowired
    private MarkingRateService markingRateService;

    @Autowired
    private ObjectMapper objectMapper;

    // Save single or multiple MarkingRates dynamically
    @PostMapping("/save")
    public ResponseEntity<?> saveMarkingRates(@RequestBody Object requestBody) {
        try {
            if (requestBody instanceof List<?>) {
                // Handle a list of MarkingRates
                List<MarkingRate> markingRates = objectMapper.convertValue(requestBody, objectMapper.getTypeFactory().constructCollectionType(List.class, MarkingRate.class));
                List<MarkingRate> savedRates = markingRateService.saveMarkingRates(markingRates);
                return ResponseEntity.ok(savedRates);
            } else {
                // Handle a single MarkingRate
                MarkingRate singleRate = objectMapper.convertValue(requestBody, MarkingRate.class);
                MarkingRate savedRate = markingRateService.saveMarkingRate(singleRate);
                return ResponseEntity.ok(savedRate);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing request: " + e.getMessage());
        }
    }

    // Get all MarkingRates
    @GetMapping("/all")
    public ResponseEntity<List<MarkingRate>> getAllMarkingRates() {
        List<MarkingRate> rates = markingRateService.getAllMarkingRates();
        return ResponseEntity.ok(rates);
    }

    // Get a MarkingRate by ID
    @GetMapping("/{id}")
    public ResponseEntity<MarkingRate> getMarkingRateById(@PathVariable Long id) {
        MarkingRate rate = markingRateService.getMarkingRateById(id);
        return (rate != null) ? ResponseEntity.ok(rate) : ResponseEntity.notFound().build();
    }
}



